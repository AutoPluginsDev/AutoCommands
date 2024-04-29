package fr.lumi.Metrics;

import java.util.function.BiConsumer;

public abstract class CustomChart {

    private final String chartId;

    protected CustomChart(String chartId) {
        if (chartId == null) {
            throw new IllegalArgumentException("chartId must not be null");
        }
        this.chartId = chartId;
    }

    public Metrics.JsonObjectBuilder.JsonObject getRequestJsonObject(
            BiConsumer<String, Throwable> errorLogger, boolean logErrors) {
        Metrics.JsonObjectBuilder builder = new Metrics.JsonObjectBuilder();
        builder.appendField("chartId", chartId);
        try {
            Metrics.JsonObjectBuilder.JsonObject data = getChartData();
            if (data == null) {
                // If the data is null we don't send the chart.
                return null;
            }
            builder.appendField("data", data);
        } catch (Throwable t) {
            if (logErrors) {
                errorLogger.accept("Failed to get data for custom chart with id " + chartId, t);
            }
            return null;
        }
        return builder.build();
    }

    protected abstract Metrics.JsonObjectBuilder.JsonObject getChartData() throws Exception;
}