import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.quranku.quranku_app.R
import com.quranku.quranku_app.data.models.HistoryItem
import com.quranku.quranku_app.ui.util.capitalizeFirstLetter
import com.quranku.quranku_app.ui.util.formatTanggal
import com.quranku.quranku_app.ui.viewmodel.HistoryViewModel
import kotlinx.coroutines.flow.distinctUntilChanged


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun HistoryScreen(historyViewModel: HistoryViewModel = hiltViewModel()) {
    val historyItems by historyViewModel.historyState.collectAsState()
    val isLoading by historyViewModel.loadingState.collectAsState()
    val isLoadingMore by historyViewModel.isLoadingMore.collectAsState()
    val errorState by historyViewModel.errorState.collectAsState()

    val listState = rememberLazyListState()

    // Pull-to-refresh state
    val refreshState = rememberPullRefreshState(
        refreshing = isLoading,
        onRefresh = { historyViewModel.fetchHistory(isFirstLoad = true) }
    )

    LaunchedEffect(Unit) {
        if (historyItems.isEmpty()) {
            historyViewModel.fetchHistory(isFirstLoad = true)
        }
    }

    listState.OnBottomReached {
        historyViewModel.fetchHistory()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "History Belajar",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(id = R.color.blue_dark),
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = colorResource(id = R.color.blue_dark)
                ),
                modifier = Modifier
                    .height(45.dp)
                    .shadow(4.dp)
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .background(Color.White)
                .pullRefresh(refreshState) // Add pullRefresh here
        ) {
            when {
                isLoading && historyItems.isEmpty() -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0x80000000)),
                        contentAlignment = Alignment.Center
                    ) {
                        // Menggeser loading indicator ke atas bottom navbar
                        Box(
                            modifier = Modifier.offset(y = (-60).dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = colorResource(id = R.color.blue_dark_light))
                        }
                    }
                }
                errorState != null && historyItems.isEmpty() -> {
                    Text(
                        text = errorState ?: "Terjadi kesalahan",
                        color = Color.Red,
                        modifier = Modifier.fillMaxSize().padding(top = 10.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 24.sp
                    )
                }
                historyItems.isEmpty() -> {
                    Text(
                        text = "Silahkan Lakukan Pembelajaran Huruf Hijaiyah Dahulu",
                        modifier = Modifier.fillMaxSize().padding(top = 10.dp),
                        color = Color.DarkGray,
                        textAlign = TextAlign.Center,
                        fontSize = 24.sp
                    )
                }
                else -> {
                    LazyColumn(
                        state = listState,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        contentPadding = PaddingValues(vertical = 10.dp),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 24.dp)
                    ) {
                        items(historyItems) { item ->
                            HistoryCard(item)
                        }

                        if (isLoadingMore) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        // Menambahkan padding bottom agar tidak tertutup bottom navbar
                                        .padding(16.dp)
                                        .padding(bottom = 20.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(32.dp).padding(top = 10.dp),
                                        color = colorResource(id = R.color.blue_dark_light)
                                    )
                                }
                            }
                        }
                        item{
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color.White)
                                    .padding(bottom = 10.dp)
                            )
                        }
                    }
                }
            }

            // Pull-to-refresh indicator
            PullRefreshIndicator(
                refreshing = isLoading,
                state = refreshState,
                modifier = Modifier.align(Alignment.TopCenter),
                contentColor = colorResource(id = R.color.blue_dark_light)
            )
        }
    }
}

// Optimized bottom detection
@Composable
fun LazyListState.OnBottomReached(
    loadMore: () -> Unit
) {
    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
                ?: return@derivedStateOf false

            // Trigger when last visible item is the last item in the list
            lastVisibleItem.index >= layoutInfo.totalItemsCount - 1
        }
    }

    LaunchedEffect(shouldLoadMore) {
        snapshotFlow { shouldLoadMore.value }
            .distinctUntilChanged()
            .collect {
                if (it) loadMore()
            }
    }
}

@Composable
fun HistoryCard(item: HistoryItem) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Icon Huruf di Tengah
            Box(
                modifier = Modifier
                    .weight(0.5f) // Atur ukuran proporsional
                    .fillMaxHeight(), // Pastikan Box memenuhi tinggi parent
                contentAlignment = Alignment.Center // Memastikan isi Box berada di tengah
            ) {
                Text(
                    text = item.huruf,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.blue_dark_light)
                )
            }

            // Middle Section
            Column(
                modifier = Modifier
                    .weight(2f) // Berikan ruang lebih besar untuk column
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = formatTanggal(item.tanggal),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.black),
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Text(
                    text = item.waktu,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = colorResource(id = R.color.blue_dark_light),
                    letterSpacing = 1.sp, // Atur jarak antar huruf
                    modifier = Modifier.padding(bottom = 2.dp)
                )
                Text(
                    text = "Kondisi : ${item.kondisi}",
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }

            // Status
            Text(
                text = capitalizeFirstLetter(item.hasil) ,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .weight(1f) // Atur ukuran proporsional
                    .background(
                        color = if (item.hasil == "benar") colorResource(id = R.color.green) else colorResource(id = R.color.red),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun HistoryScreenPreview() {
    HistoryScreen()
}
