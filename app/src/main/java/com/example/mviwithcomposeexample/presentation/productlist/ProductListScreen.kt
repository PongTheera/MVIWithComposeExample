@file:OptIn(ExperimentalMaterialApi::class)

package com.example.mviwithcomposeexample.presentation.productlist

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mviwithcomposeexample.R
import com.example.mviwithcomposeexample.TopAppBarActionEnum
import com.example.mviwithcomposeexample.data.model.ProductResponseModel
import com.example.mviwithcomposeexample.domain.mapper.ProductToDomainMapper
import com.example.mviwithcomposeexample.domain.model.ProductModel
import com.example.mviwithcomposeexample.domain.model.ProductOperator
import com.example.mviwithcomposeexample.presentation.productdialog.ProductDialog
import com.example.mviwithcomposeexample.presentation.productlist.component.ProductItem
import com.example.mviwithcomposeexample.ui.component.LoadingDialog
import com.example.mviwithcomposeexample.ui.component.WarningComponent
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProductListRoute(
    viewModel: ProductListViewModel = koinViewModel(),
    onProductClick: (Int) -> Unit,
    onAddProductSuccess: () -> Unit,
    currentTopAppBarAction: TopAppBarActionEnum,
) {

    val context = LocalContext.current
    val productListUiState by viewModel.productListUiState.collectAsStateWithLifecycle()
    val productListEffect by viewModel.productListEffect.collectAsStateWithLifecycle(ProductListEffect.Idle)

    if (viewModel.showProductActionDialog) {
        viewModel.getSelectedProduct()?.let { product ->
            ProductDialog(
                product = product,
                onDeleteProductClicked = {
                    val actionIntent = ProductListIntent.DeleteProduct(productId = product.id)
                    viewModel.sendIntent(productListIntent = actionIntent)
                    viewModel.showProductActionDialog = false
                },
                onUpdateProductClicked = {
                    val actionIntent = ProductListIntent.UpdateProduct(productId = product.id)
                    viewModel.sendIntent(productListIntent = actionIntent)
                    viewModel.showProductActionDialog = false
                },
                onTestErrorFetchProductClicked = {
                    val actionIntent = ProductListIntent.FetchProductsWithError
                    viewModel.sendIntent(productListIntent = actionIntent)
                    viewModel.showProductActionDialog = false
                },
                onDismiss = {
                    viewModel.showProductActionDialog = false
                }
            )
        }
    }

    LaunchedEffect(currentTopAppBarAction) {
        when (currentTopAppBarAction) {
            TopAppBarActionEnum.ACTION_ADD_PRODUCT -> {

                val actionIntent = ProductListIntent.AddProduct
                viewModel.sendIntent(productListIntent = actionIntent)

            }

            else -> Unit
        }
    }

    LaunchedEffect(Unit) {
        val intent = ProductListIntent.FetchProducts
        viewModel.sendIntent(intent)
    }

    LaunchedEffect(productListEffect) {
        when (val state = productListEffect) {
            is ProductListEffect.ProcessSuccess -> {
                val operationType = state.operationType
                showToastMessage(
                    context,
                    context.getString(R.string.an_operation_is_success, operationType)
                )
                viewModel.updateProductListIdleState()
            }

            is ProductListEffect.ProcessError -> {
                showToastMessage(
                    context,
                    context.getString(R.string.an_operation_error, state.error)
                )
                viewModel.updateProductListIdleState()
            }

            else -> Unit
        }
    }

    var isPullToRefresh by rememberSaveable {
        mutableStateOf(false)
    }

    if (productListUiState is ProductListUiState.ProductError ||
        productListUiState is ProductListUiState.EmptyProductList ||
        productListUiState is ProductListUiState.Success
    ) {
        if (isPullToRefresh) {
            isPullToRefresh = false
        }
    }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = isPullToRefresh && productListUiState is ProductListUiState.ProductLoading,
        onRefresh = {
            isPullToRefresh = true
            viewModel.sendIntent(ProductListIntent.FetchProducts)
        }
    )

    ProductListScreen(
        isPullToRefresh = isPullToRefresh,
        pullRefreshState = pullRefreshState,
        onProductClick = { id ->
            onProductClick.invoke(id)
        },
        productListUiState = productListUiState,
        productListEffect = productListEffect,
        onAddProductSuccess = onAddProductSuccess,
        onShowActionDialog = { product ->
            viewModel.setSelectedProduct(product)
            viewModel.sendIntent(ProductListIntent.ProductActionDialog(product = product))
        },
        onUpdateProductClick = { productId, operator ->
            viewModel.sendIntent(
                ProductListIntent.UpdateProductQuantity(
                    productId = productId,
                    productOperator = operator
                )
            )
        }
    )

}


private fun showToastMessage(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

@Composable
fun ProductListScreen(
    modifier: Modifier = Modifier,
    onProductClick: (Int) -> Unit,
    productListUiState: ProductListUiState,
    productListEffect: ProductListEffect,
    onAddProductSuccess: () -> Unit,
    onShowActionDialog: (ProductModel) -> Unit,
    onUpdateProductClick: (productId: Int, ProductOperator) -> Unit,
    pullRefreshState: PullRefreshState,
    isPullToRefresh: Boolean,
) {

    OperationLoadingUiState(
        productListEffect = productListEffect,
        onAddProductSuccess = onAddProductSuccess,
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .pullRefresh(pullRefreshState)
    ) {

        when (productListUiState) {
            is ProductListUiState.ProductLoading -> {
                LoadingDialog(loading = true)
            }

            is ProductListUiState.ProductError -> {
                LoadingDialog(loading = false)
                ShowProductError(error = productListUiState.error)
            }

            is ProductListUiState.Success -> {
                LoadingDialog(loading = false)
                ProductList(
                    productList = productListUiState.productList,
                    onProductClick = onProductClick,
                    onShowActionDialog = onShowActionDialog,
                    onUpdateProductQuantityClick = onUpdateProductClick
                )
            }

            is ProductListUiState.EmptyProductList -> {
                LoadingDialog(loading = false)
                ShowProductEmpty()
            }
        }

        val isRefreshing = isPullToRefresh && productListUiState is ProductListUiState.ProductLoading

        PullRefreshIndicator(
            refreshing = isRefreshing,
            state = pullRefreshState,
            modifier = Modifier.align(
                Alignment.TopCenter
            )
        )

    }

}

@Composable
fun ShowProductEmpty(
    modifier: Modifier = Modifier,
) {
    WarningComponent(modifier, message = stringResource(R.string.no_products))
}

@Composable
fun ShowProductError(modifier: Modifier = Modifier, error: Throwable?) {
    WarningComponent(
        modifier = modifier,
        message = stringResource(R.string.an_error_occured, error?.message.orEmpty())
    )
}

@Composable
private fun OperationLoadingUiState(
    productListEffect: ProductListEffect,
    onAddProductSuccess: () -> Unit,
) {
    when (productListEffect) {
        is ProductListEffect.ProcessLoading -> {
            LoadingDialog(loading = true, message = stringResource(R.string.process_loading))
        }

        is ProductListEffect.ProcessSuccess -> {
            LoadingDialog(loading = false)
            onAddProductSuccess.invoke()
        }

        is ProductListEffect.ProcessError -> {
            LoadingDialog(loading = false)
        }

        else -> Unit
    }
}

@Composable
fun ProductList(
    productList: List<ProductModel>,
    onProductClick: (Int) -> Unit,
    onShowActionDialog: (ProductModel) -> Unit,
    onUpdateProductQuantityClick: (productId: Int, ProductOperator) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2)
    ) {
        items(productList) { product ->
            ProductItem(
                product = product,
                onProductClick = onProductClick,
                onShowActionDialog = onShowActionDialog,
                onUpdateProductQuantityClick = onUpdateProductQuantityClick
            )
        }
    }
}

@Preview
@Composable
fun ProductListScreenPreview() {

    val mapper = ProductToDomainMapper()
    val mockProductList = listOf(
        ProductResponseModel(
            id = 1,
            name = "Mango Long Long Long Name",
            40.0,
            "Mango Detail",
            total = 0
        ),
        ProductResponseModel(id = 2, name = "Mango Steen", 30.0, "Mango Steen Detail", total = 2),
        ProductResponseModel(id = 3, name = "Apple", 20.0, "Apple Detail", total = 3),
        ProductResponseModel(id = 4, name = "Banana", 50.0, "Banana Detail", total = -10),
        ProductResponseModel(id = 5, name = "Orange", 60.0, "Orange Detail", total = 0),
    ).map {
        mapper.transform(it)
    }

    val mockProductResultUiState = ProductListUiState.Success(mockProductList)
    ProductListScreen(
        onProductClick = {},
        productListUiState = mockProductResultUiState,
        productListEffect = ProductListEffect.ProcessSuccess(OperationType.ADD_PRODUCT),
        onAddProductSuccess = {},
        onShowActionDialog = {},
        onUpdateProductClick = { _, _ -> },
        pullRefreshState = rememberPullRefreshState(refreshing = false, onRefresh = { }),
        isPullToRefresh = false
    )

}

@Preview
@Composable
fun ProductListErrorPreview() {

    val mockProductResultUiState =
        ProductListUiState.ProductError(Throwable("can not connect to ther service"))
    ProductListScreen(
        onProductClick = {},
        productListUiState = mockProductResultUiState,
        productListEffect = ProductListEffect.ProcessSuccess(OperationType.ADD_PRODUCT),
        onAddProductSuccess = {},
        onShowActionDialog = {},
        onUpdateProductClick = { _, _ -> },
        pullRefreshState = rememberPullRefreshState(refreshing = false, onRefresh = { }),
        isPullToRefresh = false
    )

}