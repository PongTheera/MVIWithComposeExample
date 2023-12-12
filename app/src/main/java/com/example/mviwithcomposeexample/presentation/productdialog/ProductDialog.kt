package com.example.mviwithcomposeexample.presentation.productdialog

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.mviwithcomposeexample.R
import com.example.mviwithcomposeexample.domain.model.ProductModel

@Composable
fun ProductDialog(
    product: ProductModel,
    onDismiss: () -> Unit,
    onDeleteProductClicked: () -> Unit,
    onUpdateProductClicked: () -> Unit,
    onTestErrorFetchProductClicked: () -> Unit,
) {

    ProductUiDialog(
        onDismiss = onDismiss,
        product = product,
        onDeleteProductClicked = onDeleteProductClicked,
        onUpdateProductClicked = onUpdateProductClicked,
        onTestErrorFetchProductClicked = onTestErrorFetchProductClicked
    )

}

@Composable
fun ProductUiDialog(
    onDismiss: () -> Unit,
    product: ProductModel,
    onDeleteProductClicked: () -> Unit,
    onUpdateProductClicked: () -> Unit,
    onTestErrorFetchProductClicked: () -> Unit,
) {

    val configuration = LocalConfiguration.current

    AlertDialog(
        properties = DialogProperties(usePlatformDefaultWidth = false),
        modifier = Modifier.widthIn(max = configuration.screenWidthDp.dp - 80.dp),
        onDismissRequest = { onDismiss.invoke() },
        title = {
            Text(
                text = stringResource(R.string.product_action),
                style = MaterialTheme.typography.titleLarge,
            )
        },
        text = {
            Divider()
            Column(Modifier.verticalScroll(rememberScrollState())) {
                SettingsPanel(
                    product = product,
                    onDeleteProductClicked = onDeleteProductClicked,
                    onUpdateProductClicked = onUpdateProductClicked,
                    onTestErrorFetchProductClicked = onTestErrorFetchProductClicked
                )
                Divider(Modifier.padding(top = 8.dp))
            }
        },
        confirmButton = {
            Text(
                text = stringResource(R.string.close),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .clickable { onDismiss() },
            )
        },
    )
}

@Composable
fun SettingsPanel(
    product: ProductModel,
    onDeleteProductClicked: () -> Unit,
    onUpdateProductClicked: () -> Unit,
    onTestErrorFetchProductClicked: () -> Unit,
) {
    SettingsDialogSectionTitle(product.name)
    Column(Modifier.selectableGroup()) {
        SettingsDialogThemeChooserRow(
            text = stringResource(R.string.delete_product),
            onClick = onDeleteProductClicked,
            icon = Icons.Rounded.Delete
        )
        SettingsDialogThemeChooserRow(
            text = stringResource(R.string.update_product),
            onClick = onUpdateProductClicked,
            icon = Icons.Rounded.Edit
        )
        SettingsDialogThemeChooserRow(
            text = stringResource(R.string.test_error_fetch_the_procuct_list),
            onClick = onTestErrorFetchProductClicked,
            icon = Icons.Rounded.Warning
        )
    }
}

@Composable
private fun SettingsDialogSectionTitle(text: String, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
        )
    }
}

@Composable
fun SettingsDialogThemeChooserRow(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .clickable { onClick.invoke() }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Spacer(Modifier.width(8.dp))
        Text(text, modifier = Modifier.weight(1f))
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.alpha(0.6f)
        )
    }
}

@Preview
@Composable
fun PreviewProductDialog() {

    ProductUiDialog(
        onDismiss = {},
        product = ProductModel(
            id = 1,
            name = "Title",
            price = 50.0,
            description = "Desc",
            formattedName = "Name : Test",
            formattedPrice = "$50",
            formattedDescription = "Description : Desc",
            total = 5
        ),
        onDeleteProductClicked = {},
        onTestErrorFetchProductClicked = {},
        onUpdateProductClicked = {}
    )

}