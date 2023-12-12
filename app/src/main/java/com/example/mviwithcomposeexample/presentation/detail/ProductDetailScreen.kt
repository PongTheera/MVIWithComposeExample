package com.example.mviwithcomposeexample.presentation.detail

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.mviwithcomposeexample.R
import com.example.mviwithcomposeexample.domain.model.ProductModel
import com.example.mviwithcomposeexample.domain.model.ProductOperator
import com.example.mviwithcomposeexample.presentation.detail.component.ProductDetail
import com.example.mviwithcomposeexample.ui.component.LoadingDialog
import com.example.mviwithcomposeexample.ui.component.WarningComponent
import org.koin.androidx.compose.koinViewModel


@Composable
fun ProductDetailRoute(
    viewModel: ProductDetailViewModel = koinViewModel(),
) {

    val productDetailUiState by viewModel.productDetailUiState.collectAsStateWithLifecycle()
    val productDetailEffect by viewModel.productDetailEffect.collectAsStateWithLifecycle(
        ProductDetailEffect.Idle
    )

    val context = LocalContext.current

    ProductDetailScreen(
        productUiState = productDetailUiState,
        productDetailEffect = productDetailEffect,
        onUpdateProductClick = { id, operator ->
            val intent =
                ProductDetailIntent.UpdateProductQuantity(productId = id, productOperator = operator)
            viewModel.sendIntent(intent)
        },
        onAddButtonClick = { productModel ->
            val total = productModel.total
            Toast.makeText(
                context,
                context.getString(R.string.add_button_clicked, "$total"),
                Toast.LENGTH_SHORT
            ).show()
        },
        onShowToastMessage = { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        })

}

@Composable
internal fun ProductDetailScreen(
    modifier: Modifier = Modifier,
    productUiState: ProductDetailUiState,
    productDetailEffect: ProductDetailEffect,
    onUpdateProductClick: (productId: Int, ProductOperator) -> Unit,
    onAddButtonClick: ((ProductModel) -> Unit),
    onShowToastMessage: (String) -> Unit,
) {

    OperationState(
        productDetailEffect = productDetailEffect,
        onShowToastMessage = onShowToastMessage
    )

    Column(modifier = modifier.fillMaxSize()) {
        when (productUiState) {
            is ProductDetailUiState.Loading -> {
                LoadingDialog(loading = true)

            }

            is ProductDetailUiState.NotFoundProductUi -> {
                LoadingDialog(loading = false)

            }

            is ProductDetailUiState.Success -> {
                LoadingDialog(loading = false)

                val productModel = productUiState.productModel
                if (productModel == null) {
                    ShowProductEmpty()
                } else {
                    ProductDetail(
                        productModel = productUiState.productModel,
                        onUpdateProductClick = onUpdateProductClick,
                        onAddButtonClick = onAddButtonClick
                    )
                }

            }

            is ProductDetailUiState.Error -> {
                LoadingDialog(loading = false)
            }
        }
    }

}

@Composable
private fun OperationState(
    productDetailEffect: ProductDetailEffect,
    onShowToastMessage: (String) -> Unit,
) {
    when (productDetailEffect) {
        is ProductDetailEffect.Error -> {
            LoadingDialog(loading = false)
            onShowToastMessage.invoke(productDetailEffect.message)
        }

        is ProductDetailEffect.Success -> {
            LoadingDialog(loading = false)
        }

        is ProductDetailEffect.Loading -> {
            LoadingDialog(loading = true)
        }

        is ProductDetailEffect.Idle -> {
            LoadingDialog(loading = false)
        }
    }
}

@Composable
fun ShowProductEmpty(modifier: Modifier = Modifier) {
    WarningComponent(modifier = modifier, message = stringResource(R.string.product_is_empty))
}


@Preview("Product Detail Success")
@Composable
fun PreviewProductDetailSuccess() {

    ProductDetailScreen(
        productUiState = ProductDetailUiState.Success(
            productModel = ProductModel(
                id = 1,
                name = "Mango Long product product name",
                price = 3000000.0,
                description = "Mango Long Detail",
                formattedDescription = "Description : Mango Long Detail",
                formattedName = "Name : Mango",
                formattedPrice = "30000 Baht",
                total = 0
            )
        ),
        productDetailEffect = ProductDetailEffect.Success,
        onUpdateProductClick = { _, _ -> },
        onAddButtonClick = {},
        onShowToastMessage = {}
    )

}


@Preview("Product Detail Success With Empty")
@Composable
fun PreviewProductDetailSuccessWithEmpty() {

    ProductDetailScreen(
        productUiState = ProductDetailUiState.Success(productModel = null),
        productDetailEffect = ProductDetailEffect.Success,
        onUpdateProductClick = { _, _ -> },
        onAddButtonClick = {},
        onShowToastMessage = {}
    )

}
