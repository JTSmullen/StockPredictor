package com.StockPredictor.Data;

import com.StockPredictor.Indicators.BollingerBands;
import com.StockPredictor.Indicators.RSI;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVWriter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Grabs Live data to train ML models on RSI / BB Strategy within SPY
 */
public class LiveDataFetcher {

    // TODO: Fetch live data from the market every 5 seconds

    private static final String API_KEY = ""; // This is not safe change to an env var ect. before posting on github ect.

    private static final String SYMBOL = ""; // Start getting data for one symbol

    private static final String API_URL = "https://finnhub.io/api/v1/quote";

    private static final String CSV_FILE_PATH = ""; // If choose to use CSV over In memory (recommended for data consistency)

    private static final int POLLING_INTERVAL_SECONDS = 5; // Number of seconds between each data fetch





}
