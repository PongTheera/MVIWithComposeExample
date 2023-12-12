package com.example.mviwithcomposeexample.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mviwithcomposeexample.domain.model.ProductModel
import com.example.mviwithcomposeexample.domain.model.ProductOperator
import com.example.mviwithcomposeexample.domain.model.ResultState
import com.example.mviwithcomposeexample.domain.model.asResultState
import com.example.mviwithcomposeexample.domain.usecase.GetProductByIdUseCase
import com.example.mviwithcomposeexample.domain.usecase.UpdateProductQuantityUseCase
import com.example.mviwithcomposeexample.presentation.detail.navigation.ProductDetailArgs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
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
class ProductDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val updateProductQuantityUseCase: UpdateProductQuantityUseCase,
) : ViewModel() {

    private val productDetailArgs: ProductDetailArgs = ProductDetailArgs(savedStateHandle)
    val productId = productDetailArgs.productId

    private val _productDetailUiState = MutableStateFlow<ProductDetailUiState>(ProductDetailUiState.Loading)
    val productDetailUiState get() = _productDetailUiState.asStateFlow()

    private val _productDetailEffect = Channel<ProductDetailEffect>()
    val productDetailEffect get() = _productDetailEffect.receiveAsFlow()

    private val userIntent = MutableSharedFlow<ProductDetailIntent>()

    init {
        handleIntent()

        val intent = ProductDetailIntent.FetchProductDetail(productId = productId)
        sendIntent(intent)
    }

    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.collectLatest { event ->
                when (event) {
                    is ProductDetailIntent.FetchProductDetail -> {

                        fetchProductDetail(productId = event.productId)

                    }

                    is ProductDetailIntent.UpdateProductQuantity -> {

                        updateProductQuantity(productId = event.productId, productOperator = event.productOperator)

                    }
                }
            }
        }

    }

    private fun fetchProductDetail(productId: String) {
        viewModelScope.launch {
            getProductByIdUseCase.invoke(productId = productId)
                .asResultState()
                .map(::mapToProductDetailUiState)
                .collectLatest { uiState ->
                    _productDetailUiState.emit(uiState)
                }
        }
    }

    private fun mapToProductDetailUiState(resultState: ResultState<ProductModel?>) =
        when (resultState) {
            is ResultState.Error -> {
                ProductDetailUiState.Error(resultState.exception?.message.orEmpty())
            }

            is ResultState.Loading -> {
                ProductDetailUiState.Loading
            }

            is ResultState.Success -> {
                val product = resultState.data
                if (product == null) {
                    ProductDetailUiState.NotFoundProductUi
                } else {
                    ProductDetailUiState.Success(product)
                }
            }
        }

    private fun updateProductQuantity(productId: Int, productOperator: ProductOperator) {
        viewModelScope.launch {
            updateProductQuantityUseCase.invoke(productId = productId, productOperator = productOperator)
                .onStart {
                    _productDetailEffect.send(ProductDetailEffect.Loading)
                }
                .catch { error ->
                    _productDetailEffect.send(ProductDetailEffect.Error(message = error.message.orEmpty()))
                }
                .flatMapLatest {
                    getProductByIdUseCase.invoke(productId = "$productId")
                }
                .collectLatest { model ->
                    _productDetailEffect.send(ProductDetailEffect.Success)
                    _productDetailUiState.emit(ProductDetailUiState.Success(productModel = model))
                }
        }
    }

    fun sendIntent(productDetailIntent: ProductDetailIntent) {
        viewModelScope.launch {
            userIntent.emit(productDetailIntent)
        }
    }

}

