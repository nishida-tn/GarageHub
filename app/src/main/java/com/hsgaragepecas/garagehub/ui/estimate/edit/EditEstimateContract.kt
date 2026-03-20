package com.hsgaragepecas.garagehub.ui.estimate.edit

import com.hsgaragepecas.garagehub.data.model.EstimateFullDto
import com.hsgaragepecas.garagehub.data.model.EstimateItemDto
import com.hsgaragepecas.garagehub.data.model.ProposalDto

/**
 * The contract for the edit estimate screen.
 */
interface EditEstimateContract {

    /**
     * The state of the edit estimate screen.
     */
    data class EditEstimateUiState(
        val isLoading: Boolean = false,
        val estimate: EstimateFullDto? = null,
        val items: List<EstimateItemDto> = emptyList(),
        val photos: List<String> = emptyList(),
        val proposals: List<ProposalDto> = emptyList(),
        val error: String? = null,
        val isSaving: Boolean = false
    )

    /**
     * The events for the edit estimate screen.
     */
    sealed interface EditEstimateUiEvent {
        /**
         * Event to show a toast message.
         *
         * @param message The message to show.
         */
        data class ShowToast(val message: String) : EditEstimateUiEvent

        /**
         * Event to navigate back.
         */
        data object NavigateBack : EditEstimateUiEvent
    }

    /**
     * The intents for the edit estimate screen.
     */
    sealed interface EditEstimateUiIntent {
        /**
         * Intent to load the estimate details.
         *
         * @param estimateId The ID of the estimate.
         */
        data class LoadEstimate(val estimateId: Int) : EditEstimateUiIntent

        /**
         * Intent to save the estimate.
         */
        data object SaveEstimate : EditEstimateUiIntent

        /**
         * Intent to delete an item from the estimate.
         *
         * @param itemId The ID of the item.
         */
        data class DeleteItem(val itemId: Int) : EditEstimateUiIntent

        /**
         * Intent to add an item to the estimate.
         *
         * @param item The item to add.
         */
        data class AddItem(val item: EstimateItemDto) : EditEstimateUiIntent

        /**
         * Intent to generate a PDF for the estimate.
         */
        data object GeneratePdf : EditEstimateUiIntent

        /**
         * Intent to send the estimate via WhatsApp.
         */
        data object SendWhatsApp : EditEstimateUiIntent

        /**
         * Intent to make an order from the estimate.
         */
        data object MakeOrder : EditEstimateUiIntent

        /**
         * Intent to create a demand from the estimate.
         */
        data object CreateDemand : EditEstimateUiIntent
    }
}
