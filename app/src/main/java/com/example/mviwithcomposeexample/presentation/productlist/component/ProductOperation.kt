package com.example.mviwithcomposeexample.presentation.productlist.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.mviwithcomposeexample.data.model.ProductResponseModel
import com.example.mviwithcomposeexample.domain.mapper.ProductToDomainMapper
import com.example.mviwithcomposeexample.domain.model.ProductModel
import com.example.mviwithcomposeexample.domain.model.ProductOperator

@Composable
fun ProductOperation(
    modifier: Modifier = Modifier,
    product: ProductModel,
    onUpdateProductTotalClick: (Int: Int, ProductOperator) -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        IconButton(onClick = {
            onUpdateProductTotalClick.invoke(product.id, ProductOperator.DELETE)
        }) {
            Icon(Icons.Default.KeyboardArrowLeft, contentDescription = null)
        }

        Text("${product.total}", fontSize = 20.sp, fontWeight = FontWeight.Normal)

        IconButton(onClick = {
            onUpdateProductTotalClick.invoke(product.id, ProductOperator.ADD)
        }) {
            Icon(Icons.Default.KeyboardArrowRight, contentDescription = null)
        }

    }
}

@Preview
@Composable
fun PreviewProductOperation() {

    val responseModel =
        ProductResponseModel(id = 2, name = "Mango Steen", 30.0, "Mango Steen Detail", total = 2)
    val mapper = ProductToDomainMapper()

    ProductOperation(
        product = mapper.transform(responseModel),
        onUpdateProductTotalClick = { _, _ -> }
    )

}