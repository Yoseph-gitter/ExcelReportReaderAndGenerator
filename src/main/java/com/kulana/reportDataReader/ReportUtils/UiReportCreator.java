package com.kulana.reportDataReader.ReportUtils;

import java.util.List;

public class UiReportCreator extends ReportCreator {
    public UiReportCreator(List<ReportRun> reportRuns, int volume, String expectedBatchSize) {
        super(reportRuns);
    }

    public UiReportCreator(List<ReportRun> reportRuns) {
        this(reportRuns, 0, "0");
    }
}
