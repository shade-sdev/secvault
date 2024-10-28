package ui.components.forms.passwordmgnt

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.koinScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import repository.user.User
import ui.components.DropdownItem
import ui.components.FormDropdown
import ui.components.FormTextArea
import ui.components.FormTextField
import ui.screens.SecVaultScreen
import ui.theme.Font
import ui.theme.primary
import ui.theme.secondary
import ui.theme.tertiary
import ui.validators.CreditCardFormFieldName
import ui.validators.creditCardFormValidator
import viewmodel.PasswordMgntScreenModel

class CreditCardForm : Screen {

    @Composable
    override fun Content() {

        val formValidator = remember { creditCardFormValidator() }
        val isFormValid by formValidator.isValid
        val screenModel = koinScreenModel<PasswordMgntScreenModel>()
        val navigator = LocalNavigator.current
        var selectedItem by remember { mutableStateOf<DropdownItem<User>?>(null) }
        val users by remember { mutableStateOf<List<User>?>(screenModel.fetchUsers()) }

        val cardBank = formValidator.getField(CreditCardFormFieldName.CARD_NAME)
        val cardOwner = formValidator.getField(CreditCardFormFieldName.CARD_OWNER)
        val cardNumber = formValidator.getField(CreditCardFormFieldName.CARD_NUMBER)
        val cvc = formValidator.getField(CreditCardFormFieldName.CARD_CVC)
        val pin = formValidator.getField(CreditCardFormFieldName.CARD_PIN)
        val expiry = formValidator.getField(CreditCardFormFieldName.CARD_EXPIRY)
        val notes = formValidator.getField(CreditCardFormFieldName.CARD_NOTES)

        LaunchedEffect(selectedItem) {
            selectedItem?.let {
                cardOwner?.value?.value = it.id.toString()
                formValidator.validateField(CreditCardFormFieldName.CARD_OWNER)
            }
        }

        Column(
            modifier = Modifier.fillMaxSize()
                .background(secondary)
        )
        {

            Row(
                modifier = Modifier.weight(1f)
                    .fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Header(
                    creditCardButtonShown = false,
                    notesButtonShown = true
                )
            }

            Row(
                modifier = Modifier.weight(7.5f)
                    .background(primary)
                    .fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.background(primary)
                        .padding(PaddingValues(end = 20.dp)),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically)
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(25.dp)) {
                        Column(modifier = Modifier.height(80.dp)) {
                            FormTextField(
                                value = cardBank?.value?.value ?: "",
                                onValueChange = { newValue ->
                                    cardBank?.value?.value = newValue
                                    formValidator.validateField(CreditCardFormFieldName.CARD_NAME)
                                },
                                label = CreditCardFormFieldName.CARD_NAME.fieldName,
                                icon = Icons.Filled.Money,
                                modifier = Modifier.height(45.dp).width(400.dp)
                            )
                            cardBank?.error?.value?.let {
                                Text(
                                    text = it,
                                    color = Color.Red,
                                    fontFamily = Font.RobotoRegular,
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }
                        Column(modifier = Modifier.height(80.dp)) {
                            FormDropdown(
                                items = users?.map { DropdownItem(it, it.userName) } ?: emptyList(),
                                selectedItem = selectedItem,
                                onItemSelected = { selectedItem = it },
                                modifier = Modifier.height(45.dp).width(400.dp),
                                label = CreditCardFormFieldName.CARD_OWNER.fieldName,
                                icon = Icons.Filled.VerifiedUser
                            )
                            cardOwner?.error?.value?.let {
                                Text(
                                    text = it,
                                    color = Color.Red,
                                    fontFamily = Font.RobotoRegular,
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(25.dp)) {
                        Column(modifier = Modifier.height(80.dp)) {
                            FormTextField(
                                value = cardNumber?.value?.value ?: "",
                                onValueChange = { newValue ->
                                    cardNumber?.value?.value = newValue
                                    formValidator.validateField(CreditCardFormFieldName.CARD_NUMBER)
                                },
                                label = CreditCardFormFieldName.CARD_NUMBER.fieldName,
                                icon = Icons.Filled.CreditCard,
                                modifier = Modifier.height(45.dp).width(400.dp)
                            )
                            cardNumber?.error?.value?.let {
                                Text(
                                    text = it,
                                    color = Color.Red,
                                    fontFamily = Font.RobotoRegular,
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }
                        Column(modifier = Modifier.height(80.dp)) {
                            FormTextField(
                                value = cvc?.value?.value ?: "",
                                onValueChange = { newValue ->
                                    cvc?.value?.value = newValue
                                    formValidator.validateField(CreditCardFormFieldName.CARD_CVC)
                                },
                                label = CreditCardFormFieldName.CARD_CVC.fieldName,
                                icon = Icons.Filled.Pin,
                                modifier = Modifier.height(45.dp).width(400.dp)
                            )
                            cvc?.error?.value?.let {
                                Text(
                                    text = it,
                                    color = Color.Red,
                                    fontFamily = Font.RobotoRegular,
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(25.dp)) {
                        Column(modifier = Modifier.height(80.dp)) {
                            FormTextField(
                                value = pin?.value?.value ?: "",
                                onValueChange = { newValue ->
                                    pin?.value?.value = newValue
                                    formValidator.validateField(CreditCardFormFieldName.CARD_PIN)
                                },
                                label = CreditCardFormFieldName.CARD_PIN.fieldName,
                                icon = Icons.Filled.Pin,
                                modifier = Modifier.height(45.dp).width(400.dp)
                            )
                            pin?.error?.value?.let {
                                Text(
                                    text = it,
                                    color = Color.Red,
                                    fontFamily = Font.RobotoRegular,
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }
                        Column(modifier = Modifier.height(80.dp)) {
                            FormTextField(
                                value = expiry?.value?.value ?: "",
                                onValueChange = { newValue ->
                                    expiry?.value?.value = newValue
                                    formValidator.validateField(CreditCardFormFieldName.CARD_EXPIRY)
                                },
                                label = CreditCardFormFieldName.CARD_EXPIRY.fieldName,
                                icon = Icons.Filled.DateRange,
                                modifier = Modifier.height(45.dp).width(400.dp)
                            )
                            expiry?.error?.value?.let {
                                Text(
                                    text = it,
                                    color = Color.Red,
                                    fontFamily = Font.RobotoRegular,
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(25.dp)) {
                        Column(modifier = Modifier.height(120.dp)) {
                            FormTextArea(
                                value = notes?.value?.value ?: "",
                                onValueChange = { newValue ->
                                    notes?.value?.value = newValue
                                    formValidator.validateField(CreditCardFormFieldName.CARD_NOTES)
                                },
                                label = CreditCardFormFieldName.CARD_NOTES.fieldName,
                                modifier = Modifier.width(400.dp)
                            )
                            notes?.error?.value?.let {
                                Text(
                                    text = it,
                                    color = Color.Red,
                                    fontFamily = Font.RobotoRegular,
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }
                    }

                }
            }

            Row(
                modifier = Modifier.weight(1.5f)
                    .fillMaxSize()
                    .background(tertiary),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(30.dp, Alignment.CenterHorizontally)
            ) {
                Footer(
                    { },
                    { navigator?.popUntil { screen: Screen -> screen.key == SecVaultScreen().key } },
                    isFormValid
                )
            }

        }
    }

}