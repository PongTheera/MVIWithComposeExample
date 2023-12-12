package com.example.mviwithcomposeexample.presentation.productlist.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mviwithcomposeexample.R
import com.example.mviwithcomposeexample.data.model.ProductResponseModel
import com.example.mviwithcomposeexample.domain.mapper.ProductToDomainMapper
import com.example.mviwithcomposeexample.domain.model.ProductModel
import com.example.mviwithcomposeexample.domain.model.ProductOperator

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductItem(
    modifier: Modifier = Modifier,
    product: ProductModel,
    onProductClick: (Int) -> Unit,
    onShowActionDialog: (ProductModel) -> Unit,
    onUpdateProductQuantityClick: (productId: Int, ProductOperator) -> Unit,
) {
    Column(
        modifier = Modifier.padding(8.dp)
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .combinedClickable(onClick = {
                    onProductClick.invoke(product.id)
                }, onLongClick = {
                    onShowActionDialog.invoke(product)
                }),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {

            Column(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_photo_size_select_actual_96),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(124.dp)
                )

                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = product.name,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = product.formattedPrice,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Light
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    if (product.total <= 0) {
                        AddProductButton(
                            product = product,
                            onUpdateProductTotalClick = onUpdateProductQuantityClick
                        )
                    } else {
                        ProductOperation(
                            product = product,
                            onUpdateProductTotalClick = onUpdateProductQuantityClick
                        )
                    }

                }

            }
        }
    }

}

@Preview
@Composable
fun PreviewProductItem() {

    val responseModel =
        ProductResponseModel(id = 2, name = "Mango Steen", 30.0, "Mango Steen Detail", total = 2)
    val mapper = ProductToDomainMapper()

    ProductItem(
        product = mapper.transform(responseModel),
        onProductClick = {},
        onShowActionDialog = {},
        onUpdateProductQuantityClick = { _, _ -> },
    )

}

@Preview
@Composable
fun PreviewProductItemWithTotal0() {

    val responseModel =
        ProductResponseModel(id = 2, name = "Mango Steen", 30.0, "Mango Steen Detail", total = 0)
    val mapper = ProductToDomainMapper()

    ProductItem(
        product = mapper.transform(responseModel),
        onProductClick = {},
        onShowActionDialog = {},
        onUpdateProductQuantityClick = { _, _ -> },
    )

}