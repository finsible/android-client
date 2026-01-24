package com.itsjeel01.finsiblefrontend.ui.screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.common.Currency
import com.itsjeel01.finsiblefrontend.common.CurrencyFormatter
import com.itsjeel01.finsiblefrontend.common.TransactionType
import com.itsjeel01.finsiblefrontend.common.toAmountOnly
import com.itsjeel01.finsiblefrontend.data.di.hiltCurrencyFormatter
import com.itsjeel01.finsiblefrontend.ui.component.TransactionListItem
import com.itsjeel01.finsiblefrontend.ui.model.DateAggregates
import com.itsjeel01.finsiblefrontend.ui.model.DateFilterMode
import com.itsjeel01.finsiblefrontend.ui.model.TransactionUiModel
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import com.itsjeel01.finsiblefrontend.ui.theme.expanded
import com.itsjeel01.finsiblefrontend.ui.theme.extraBold
import com.itsjeel01.finsiblefrontend.ui.theme.medium
import com.itsjeel01.finsiblefrontend.ui.theme.normal
import com.itsjeel01.finsiblefrontend.ui.theme.relaxed
import com.itsjeel01.finsiblefrontend.ui.util.DateUtils
import com.itsjeel01.finsiblefrontend.ui.viewmodel.TransactionsViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.coroutines.flow.distinctUntilChanged
import java.math.BigDecimal
import java.util.Locale.getDefault

@Composable
fun TransactionsScreen(
    viewModel: TransactionsViewModel,
    modifier: Modifier = Modifier
) {
    val currencyFormatter = hiltCurrencyFormatter()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    // Auto-load more logic
    LaunchedEffect(listState) {
        snapshotFlow {
            val lastVisibleIndex = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totalItems = listState.layoutInfo.totalItemsCount
            lastVisibleIndex >= totalItems - 15 && uiState.hasMoreData && !uiState.isLoadingMore
        }
            .distinctUntilChanged()
            .collect { shouldLoad ->
                if (shouldLoad) {
                    viewModel.loadMoreTransactions()
                }
            }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = FinsibleTheme.dimes.d16, vertical = FinsibleTheme.dimes.d12)
    ) {
        Text("History", style = FinsibleTheme.typography.t24.extraBold())

        Spacer(Modifier.height(FinsibleTheme.dimes.d16))

        when {
            uiState.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = FinsibleTheme.colors.brandAccent40)
                }
            }

            uiState.error != null && uiState.transactions.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = uiState.error ?: "Unknown error", color = FinsibleTheme.colors.error)
                }
            }

            uiState.transactions.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No transactions yet", color = FinsibleTheme.colors.secondaryContent)
                }
            }

            else -> {
                TransactionList(
                    transactions = uiState.transactions,
                    groupedTransactions = uiState.groupedTransactions,
                    isLoadingMore = uiState.isLoadingMore,
                    dateFilterModes = uiState.dateFilterModes,
                    dateAggregates = uiState.dateAggregates,
                    onToggleDateFilter = { viewModel.toggleDateFilter(it) },
                    currencyFormatter = currencyFormatter,
                    listState = listState,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun TransactionList(
    transactions: ImmutableList<TransactionUiModel>,
    groupedTransactions: ImmutableMap<String, ImmutableList<TransactionUiModel>>,
    isLoadingMore: Boolean,
    dateFilterModes: ImmutableMap<String, DateFilterMode>,
    dateAggregates: ImmutableMap<String, DateAggregates>,
    onToggleDateFilter: (String) -> Unit,
    currencyFormatter: CurrencyFormatter,
    listState: LazyListState,
    modifier: Modifier = Modifier
) {
    val finalGroupedTransactions = groupedTransactions.ifEmpty {
        transactions.groupBy { DateUtils.formatDateHeader(it.transactionDate) }
    }

    val cornerRadius = FinsibleTheme.dimes.d16

    LazyColumn(
        modifier = modifier,
        state = listState,
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        finalGroupedTransactions.forEach { (dateHeader, transactionsForDate) ->

            stickyHeader(key = "header_$dateHeader") {
                val filterMode = dateFilterModes[dateHeader] ?: DateFilterMode.NET
                val aggregates = dateAggregates[dateHeader]

                Box(modifier = Modifier.background(FinsibleTheme.colors.primaryBackground)) {
                    DateHeader(
                        dateText = dateHeader,
                        filterMode = filterMode,
                        incomeSum = aggregates?.incomeSum ?: BigDecimal.ZERO,
                        expenseSum = aggregates?.expenseSum ?: BigDecimal.ZERO,
                        netSum = aggregates?.netSum ?: BigDecimal.ZERO,
                        onToggleFilter = { onToggleDateFilter(dateHeader) },
                        currencyFormatter = currencyFormatter,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = FinsibleTheme.dimes.d12, top = FinsibleTheme.dimes.d16)
                    )
                }
            }

            val filterMode = dateFilterModes[dateHeader] ?: DateFilterMode.NET
            val visibleTransactions = when (filterMode) {
                DateFilterMode.NET -> transactionsForDate
                DateFilterMode.INCOME -> transactionsForDate.filter { it.type == TransactionType.INCOME }
                DateFilterMode.EXPENSE -> transactionsForDate.filter { it.type == TransactionType.EXPENSE }
            }

            itemsIndexed(
                items = visibleTransactions,
                key = { _, item -> item.id }
            ) { index, transaction ->

                val isFirst = index == 0
                val isLast = index == visibleTransactions.lastIndex

                val shape = when {
                    isFirst && isLast -> RoundedCornerShape(cornerRadius)
                    isFirst -> RoundedCornerShape(topStart = cornerRadius, topEnd = cornerRadius)
                    isLast -> RoundedCornerShape(bottomStart = cornerRadius, bottomEnd = cornerRadius)
                    else -> RectangleShape
                }

                Box(
                    modifier = Modifier
                        .animateItem()
                        .fillMaxWidth()
                        .clip(shape)
                        .background(FinsibleTheme.colors.surfaceContainerLow)
                        .border(
                            width = FinsibleTheme.dimes.d1,
                            color = FinsibleTheme.colors.divider,
                            shape = shape
                        )
                ) {
                    Column {
                        TransactionListItem(
                            transaction = transaction,
                            modifier = Modifier.padding(horizontal = FinsibleTheme.dimes.d16)
                        )

                        if (!isLast) {
                            HorizontalDivider(
                                color = FinsibleTheme.colors.divider,
                                thickness = FinsibleTheme.dimes.d1,
                                modifier = Modifier.padding(horizontal = FinsibleTheme.dimes.d16)
                            )
                        }
                    }
                }

                if (isLast) {
                    Spacer(modifier = Modifier.height(FinsibleTheme.dimes.d16))
                }
            }
        }

        if (isLoadingMore) {
            item(key = "loading_more") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = FinsibleTheme.colors.brandAccent40)
                }
            }
        }
    }
}

@Composable
private fun DateHeader(
    dateText: String,
    filterMode: DateFilterMode,
    incomeSum: BigDecimal,
    expenseSum: BigDecimal,
    netSum: BigDecimal,
    onToggleFilter: () -> Unit,
    currencyFormatter: CurrencyFormatter,
    modifier: Modifier = Modifier
) {
    val displayAmount = when (filterMode) {
        DateFilterMode.NET -> netSum
        DateFilterMode.INCOME -> incomeSum
        DateFilterMode.EXPENSE -> expenseSum
    }

    val targetColor = when (filterMode) {
        DateFilterMode.NET -> FinsibleTheme.colors.secondaryContent
        DateFilterMode.INCOME -> FinsibleTheme.colors.income
        DateFilterMode.EXPENSE -> FinsibleTheme.colors.expense
    }

    val animatedColor by animateColorAsState(
        targetValue = targetColor,
        animationSpec = tween(durationMillis = 150),
        label = "amount_color"
    )

    Row(
        modifier = modifier.padding(end = FinsibleTheme.dimes.d16),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = dateText.uppercase(getDefault()),
            style = FinsibleTheme.typography.t12.medium().expanded(),
            color = FinsibleTheme.colors.tertiaryContent
        )

        Row(
            modifier = Modifier
                .clickable(onClick = onToggleFilter)
                .padding(horizontal = FinsibleTheme.dimes.d4),
            horizontalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d2),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = filterMode.name,
                textAlign = TextAlign.End,
                style = FinsibleTheme.typography.t12.normal().relaxed(),
                color = FinsibleTheme.colors.secondaryContent
            )

            Icon(
                modifier = Modifier.size(FinsibleTheme.dimes.d14),
                painter = painterResource(R.drawable.ic_caret_up_down),
                tint = FinsibleTheme.colors.secondaryContent,
                contentDescription = "Change view"
            )

            Text(
                text = formatAmount(displayAmount, filterMode, currencyFormatter),
                style = FinsibleTheme.typography.t14.normal().relaxed(),
                color = animatedColor
            )
        }
    }
}

private fun formatAmount(amount: BigDecimal, mode: DateFilterMode, currencyFormatter: CurrencyFormatter, currency: Currency = Currency.INR): String {
    val formattedAmount = amount.abs().toPlainString().toAmountOnly(currencyFormatter)
    return when (mode) {
        DateFilterMode.NET -> {
            val sign = if (amount >= BigDecimal.ZERO) "+" else "-"
            "$sign ${currency.getSymbol()}$formattedAmount"
        }

        DateFilterMode.INCOME -> "+ ${currency.getSymbol()}$formattedAmount"
        DateFilterMode.EXPENSE -> "- ${currency.getSymbol()}$formattedAmount"
    }
}