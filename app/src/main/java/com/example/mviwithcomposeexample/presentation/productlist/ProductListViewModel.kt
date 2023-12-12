package com.example.mviwithcomposeexample.presentation.productlist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mviwithcomposeexample.domain.model.ProductModel
import com.example.mviwithcomposeexample.domain.model.ProductOperator
import com.example.mviwithcomposeexample.domain.model.ResultState
import com.example.mviwithcomposeexample.domain.model.asResultState
import com.example.mviwithcomposeexample.domain.usecase.AddProductUseCase
import com.example.mviwithcomposeexample.domain.usecase.DeleteProductUseCase
import com.example.mviwithcomposeexample.domain.usecase.GetAllProductsUseCase
import com.example.mviwithcomposeexample.domain.usecase.UpdateProductQuantityUseCase
import com.example.mviwithcomposeexample.domain.usecase.UpdateProductUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ProductListViewModel(
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val addProductUseCase: AddProductUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    private val updateProductUseCase: UpdateProductUseCase,
    private val updateProductQuantityUseCase: UpdateProductQuantityUseCase,
) : ViewModel() {

    private val userIntent = MutableSharedFlow<ProductListIntent>()

    private val _productListUiState =
        MutableStateFlow<ProductListUiState>(ProductListUiState.ProductLoading)
    val productListUiState get() = _productListUiState.asStateFlow()

    private val _productListEffect = Channel<ProductListEffect>()
    val productListEffect get() = _productListEffect.receiveAsFlow()

    var showProductActionDialog by mutableStateOf(false)

    private var productUiState: ProductUiState = ProductUiState()

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.collectLatest { event ->
                when (event) {
                    is ProductListIntent.FetchProducts -> {
                        fetchProducts()
                    }

                    is ProductListIntent.FetchProductsWithError -> {
                        fetchProductsWithError()
                    }

                    is ProductListIntent.AddProduct -> {
                        addProduct()
                    }

                    is ProductListIntent.DeleteProduct -> {
                        deleteProduct(id = event.productId)
                    }

                    is ProductListIntent.ProductActionDialog -> {
                        showProductDialog(event)
                    }

                    is ProductListIntent.UpdateProduct -> {
                        updateProduct(productId = event.productId)
                    }

                    is ProductListIntent.UpdateProductQuantity -> {
                        updateProductQuantity(
                            productId = event.productId,
                            productOperator = event.productOperator
                        )
                    }

                    else -> Unit
                }
            }
        }
    }

    private fun fetchProductsWithError() {
        viewModelScope.launch {
            _productListEffect.send(ProductListEffect.ProcessLoading)
            delay(500L)
            val error = Throwable("Can not access the API")
            _productListEffect.send(ProductListEffect.ProcessError(error))
            _productListUiState.emit(ProductListUiState.ProductError(error))
        }
    }

    private fun showProductDialog(event: ProductListIntent.ProductActionDialog) {
        val product = event.product
        setSelectedProduct(product)
        showProductActionDialog = true
    }

    private fun updateProduct(productId: Int) {
        viewModelScope.launch {
            updateProductUseCase.invoke(productId = productId)
                .onStart {
                    _productListEffect.send(ProductListEffect.ProcessLoading)
                }
                .catch {
                    val processErrorState = ProductListEffect.ProcessError(it)
                    _productListEffect.send(processErrorState)
                }
                .flatMapLatest {
                    getAllProductsUseCase.invoke()
                }
                .collectLatest { productListResultState ->
                    setSelectedProduct(null)
                    _productListEffect.send(
                        ProductListEffect.ProcessSuccess(
                            OperationType.UPDATE_PRODUCT_DETAIL
                        )
                    )
                    updateProductList(productListResultState)
                }

        }
    }

    private fun deleteProduct(id: Int) {
        viewModelScope.launch {
            deleteProductUseCase.invoke(id = id)
                .onStart {
                    _productListEffect.send(ProductListEffect.ProcessLoading)
                }
                .catch {
                    val processErrorState = ProductListEffect.ProcessError(it)
                    _productListEffect.send(processErrorState)
                }
                .flatMapLatest {
                    getAllProductsUseCase.invoke()
                }
                .collectLatest { productListResultState ->
                    setSelectedProduct(null)
                    _productListEffect.send(
                        ProductListEffect.ProcessSuccess(
                            OperationType.DELETE_PRODUCT
                        )
                    )
                    updateProductList(productListResultState)
                }
        }
    }

    private fun addProduct() {
        viewModelScope.launch {
            addProductUseCase.invoke()
                .onStart {
                    _productListEffect.send(ProductListEffect.ProcessLoading)
                }
                .catch {
                    val processErrorState = ProductListEffect.ProcessError(it)
                    _productListEffect.send(processErrorState)
                }
                .flatMapLatest {
                    getAllProductsUseCase.invoke()
                }
                .collectLatest { productListResultState ->
                    _productListEffect.send(
                        ProductListEffect.ProcessSuccess(
                            OperationType.ADD_PRODUCT
                        )
                    )
                    updateProductList(productListResultState)
                }
        }
    }

    private suspend fun updateProductList(productList: List<ProductModel>) {
        val productState = if (productList.isEmpty()) {
            ProductListUiState.EmptyProductList
        } else {
            ProductListUiState.Success(productList)
        }
        _productListUiState.emit(productState)
    }

    private fun fetchProducts() {
        viewModelScope.launch {
            getAllProductsUseCase.invoke()
                .asResultState()
                .map(::mapToFetchProducUiState)
                .collectLatest { fetchProductState ->
                    _productListUiState.emit(fetchProductState)
                }
        }
    }

    private fun mapToFetchProducUiState(resultState: ResultState<List<ProductModel>>) =
        when (resultState) {
            is ResultState.Loading -> {
                ProductListUiState.ProductLoading
            }

            is ResultState.Success -> {
                if (resultState.data.isEmpty()) {
                    ProductListUiState.EmptyProductList
                } else {
                    ProductListUiState.Success(resultState.data)
                }
            }

            is ResultState.Error -> {
                ProductListUiState.ProductError(resultState.exception)
            }
        }

    private fun updateProductQuantity(productId: Int, productOperator: ProductOperator) {
        viewModelScope.launch {
            updateProductQuantityUseCase.invoke(
                productId = productId,
                productOperator = productOperator
            )
                .onStart {
                    _productListEffect.send(ProductListEffect.ProcessLoading)
                }
                .catch {
                    val processErrorState = ProductListEffect.ProcessError(it)
                    _productListEffect.send(processErrorState)
                }
                .flatMapLatest {
                    getAllProductsUseCase.invoke()
                }
                .collectLatest { productListResultState ->
                    _productListEffect.send(
                        ProductListEffect.ProcessSuccess(
                            OperationType.UPDATE_PRODUCT_NUMBER
                        )
                    )
                    updateProductList(productListResultState)
                }
        }
    }

    fun sendIntent(productListIntent: ProductListIntent) {
        viewModelScope.launch {
            userIntent.emit(productListIntent)
        }
    }

    fun setSelectedProduct(product: ProductModel?) {
        productUiState = productUiState.copy(selectedProduct = product)
    }

    fun getSelectedProduct(): ProductModel? {
        return productUiState.selectedProduct
    }

    fun updateProductListIdleState() {
        viewModelScope.launch {
            _productListEffect.send(ProductListEffect.Idle)
        }
    }

}

data class ProductUiState(
    val selectedProduct: ProductModel? = null,
)