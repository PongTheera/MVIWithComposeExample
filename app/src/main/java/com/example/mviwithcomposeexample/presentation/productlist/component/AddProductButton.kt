package com.example.mviwithcomposeexample.presentation.productlist.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mviwithcomposeexample.data.model.ProductResponseModel
import com.example.mviwithcomposeexample.domain.mapper.ProductToDomainMapper
import com.example.mviwithcomposeexample.domain.model.ProductModel
import com.example.mviwithcomposeexample.domain.model.ProductOperator

@Composable
fun AddProductButton(
    modifier: Modifier = Modifier,
    product: ProductModel,
    onUpdateProductTotalClick: (productId: Int, ProductOperator) -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center
    ) {
        IconButton(onClick = {
            onUpdateProductTotalClick.invoke(product.id, ProductOperator.ADD)
        }) {
            Icon(imageVector = Icons.Default.Add, contentDescription = null)
        }
    }
}

@Preview
@Composable
fun PreviewAddProductButton() {

    val responseModel =
        ProductResponseModel(id = 2, name = "Mango Steen", 30.0, "Mango Steen Detail", total = 2)
    val mapper = ProductToDomainMapper()

    AddProductButton(
        product = mapper.transform(responseModel),
        onUpdateProductTotalClick = { _, _ ->
        }
    )

}