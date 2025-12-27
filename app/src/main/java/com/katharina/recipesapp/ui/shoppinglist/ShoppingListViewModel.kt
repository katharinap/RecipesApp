package com.katharina.recipesapp.ui.shoppinglist

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.katharina.recipesapp.data.ShoppingListItem
import com.katharina.recipesapp.data.ShoppingListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingListViewModel
    @Inject
    constructor(
        private val shoppingListRepository: ShoppingListRepository,
    ) : ViewModel() {
        sealed class UiState {
            object Loading : UiState()

            data class Success(
                val items: List<ShoppingListItem>,
            ) : UiState()
        }

        private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
        val uiState: StateFlow<UiState> = _uiState

        var nextItem by mutableStateOf("")
            private set

        fun updateNextItem(item: String) {
            nextItem = item
        }

        init {
            loadShoppingListItems()
        }

        fun loadShoppingListItems() {
            viewModelScope.launch {
                shoppingListRepository.getAllShoppingListItems().collect { result ->
                    _uiState.value = UiState.Success(items = result)
                }
            }
        }

        fun addItem() {
            viewModelScope.launch {
                shoppingListRepository.updateShoppingListItem(ShoppingListItem(name = nextItem))
                updateNextItem("")
            }
        }

        fun toggleCheckedState(item: ShoppingListItem) {
            viewModelScope.launch {
                shoppingListRepository.updateShoppingListItem(item.copy(isChecked = !item.isChecked))
            }
        }

        fun deleteAllChecked() {
            viewModelScope.launch {
                shoppingListRepository.deleteCheckedShoppingListItems()
            }
        }
    }
