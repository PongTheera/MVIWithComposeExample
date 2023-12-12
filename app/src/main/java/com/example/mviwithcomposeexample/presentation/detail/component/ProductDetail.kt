package com.example.mviwithcomposeexample.presentation.detail.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mviwithcomposeexample.R
import com.example.mviwithcomposeexample.data.model.ProductResponseModel
import com.example.mviwithcomposeexample.domain.mapper.ProductToDomainMapper
import com.example.mviwithcomposeexample.domain.model.ProductModel
import com.example.mviwithcomposeexample.domain.model.ProductOperator

@Composable
fun ProductDetail(
    modifier: Modifier = Modifier,
    productModel: ProductModel,
    onUpdateProductClick: (productId: Int, ProductOperator) -> Unit,
    onAddButtonClick: (ProductModel) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth(),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(8.dp)) {
                Image(
                    painter = painterResource(id = R.drawable.baseline_photo_size_select_actual_96),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                productModel.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                productModel.formattedPrice,
                fontSize = 16.sp,
                fontWeight = FontWeight.Light,
                modifier = Modifier
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = modifier
                .fillMaxWidth(),
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Row(modifier = Modifier.padding(8.dp)) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        if (productModel.total != 0) onUpdateProductClick.invoke(
                            productModel.id,
                            ProductOperator.DELETE
                        )
                    }) {
                        Icon(
                            Icons.Default.KeyboardArrowLeft,
                            contentDescription = null,
                            modifier = Modifier.alpha(
                                if (productModel.total == 0) 0.3f
                                else 1f
                            )
                        )
                    }
                    Text("${productModel.total}", fontSize = 20.sp, fontWeight = FontWeight.Normal)
                    IconButton(onClick = {
                        onUpdateProductClick.invoke(productModel.id, ProductOperator.ADD)
                    }) {
                        Icon(Icons.Default.KeyboardArrowRight, contentDescription = null)
                    }
                }
                Button(modifier = Modifier, onClick = {
                    onAddButtonClick.invoke(productModel)
                }) {
                    Text(stringResource(R.string.add))
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(stringResource(R.string.details), fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(4.dp))

        Text(productModel.description, fontSize = 16.sp, fontWeight = FontWeight.Normal)

    }
}


@Preview
@Composable
fun PreviewProductDetail() {

    val productModel =
        ProductResponseModel(id = 3, name = "Apple", 20.0, "Apple Detail", total = 3)
    val mapper = ProductToDomainMapper()

    ProductDetail(
        productModel = mapper.transform(productModel),
        onUpdateProductClick = { _, _ -> },
        onAddButtonClick = {},
    )

}
