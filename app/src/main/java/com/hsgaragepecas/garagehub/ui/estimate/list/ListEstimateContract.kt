package com.hsgaragepecas.garagehub.ui.estimate.list

import com.hsgaragepecas.garagehub.data.model.EstimateDto

/**
 * The contract for the list estimate screen.
 */
interface ListEstimateContract {

    /**
     * The state of the list estimate screen.
     *
     * @param isLoading Whether the screen is loading.
     * @param estimates The list of estimates.
     * @param error The error message, if any.
     * @param searchQuery The current search query.
     * @param selectedStatus The current status filter.
     */
    data class ListEstimateUiState(
        val isLoading: Boolean = false,
        val estimates: List<EstimateDto> = emptyList(),
        val error: String? = null,
        val searchQuery: String = "",
        val selectedStatus: String? = null
    )

    /**
     * The events for the list estimate screen.
     */
    sealed interface ListEstimateUiEvent {
        /**
         * Event to show a toast message.
         *
         * @param message The message to show.
         */
        data class ShowToast(val message: String) : ListEstimateUiEvent
    }

    /**
     * The intents for the list estimate screen.
     */
    sealed interface ListEstimateUiIntent {
        /**
         * Intent to load the estimates.
         */
        data object LoadEstimates : ListEstimateUiIntent

        /**
         * Intent to search for estimates.
         *
         * @param query The search query.
         */
        data class SearchEstimates(val query: String) : ListEstimateUiIntent

        /**
         * Intent to filter by status.
         *
         * @param status The status to filter by.
         */
        data class FilterByStatus(val status: String?) : ListEstimateUiIntent

        /**
         * Intent to refresh the estimates.
         */
        data object RefreshEstimates : ListEstimateUiIntent

        /**
         * Intent to delete an estimate.
         *
         * @param estimateId The ID of the estimate to delete.
         */
        data class DeleteEstimate(val estimateId: Int) : ListEstimateUiIntent
    }
}
