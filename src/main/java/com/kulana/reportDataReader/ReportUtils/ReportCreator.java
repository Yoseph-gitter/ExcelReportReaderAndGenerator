package com.kulana.reportDataReader.ReportUtils;

import com.kulana.reportDataReader.DateUtils.DateMassage;
import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
import java.util.List;

public abstract class ReportCreator {
    @Setter
    @Getter
    private int max;
    @Setter
    @Getter
    private int min;
    @Setter
    @Getter
    private float average;
    @Setter
    @Getter
    private float tp90;
    @Setter
    @Getter
    private int sum;
    @Setter
    @Getter
    protected List<ReportRun> singleReport = null;
    @Setter
    @Getter
    private String requestType;
    @Setter
    @Getter
    protected int numberOfRequests;
    @Setter
    @Getter
    private String reportStartTime;
    @Setter
    @Getter
    private String reportEndTime;
    @Setter
    @Getter
    private int numberOfFailures;
    private static final String formatter = "\n%-45s%-12s%-12s%-12s%-12s%-16s%-12s%-15s%-15s";

    public ReportCreator(List<ReportRun> reportRuns, int timeDifference) {
        this(reportRuns, 0, "15");
    }

    public ReportCreator(List<ReportRun> reportRuns) {
        this(reportRuns, 0, "0");
        initializeReportCreator();
    }

    public ReportCreator() {
    }

    public ReportCreator(List<ReportRun> reportRuns, int volume, String expectedBatchSize) {
        this.singleReport = reportRuns;
        initializeReportCreator();
    }

    public void initializeReportCreator() {
        this.sortReportRunByResponseTIme();
        this.setAverage(calculateAverage());
        this.setMax(findMaximum());
        this.setMin(findMinimum());
        this.setTp90(calculateTp90());
        this.setSum(findTotalUpTo(this.singleReport.size()));
        //this.setRequestType(getRequestTypeOfAnyReportRun());
        this.setNumberOfRequests(this.singleReport.size());
    }

    protected String getRequestTypeOfAnyReportRun() {
        if (this.singleReport.size() >= 1) {
            return this.singleReport.get(1).getRequest();
        }
        return "No RequestType";
    }

    public void sortReportRunByResponseTIme() {
        this.singleReport.sort(new Comparator<ReportRun>() {
            @Override
            public int compare(ReportRun reportRun, ReportRun t1) {
                int reportRunSeconds = 0;
                int reportRunTwoSeconds = 0;
                try {
                    reportRunSeconds = Integer.valueOf(reportRun.getTotalProcessingTime());
                    // System.out.println("FORMATTED  +++" + t1.getTotalProcessingTime());
                    reportRunTwoSeconds = Integer.valueOf(t1.getTotalProcessingTime());
                    // System.out.println("FORMATTED  TWO +++" + t1.getTotalProcessingTime());
                } catch (NumberFormatException e) {
                    reportRunSeconds = DateMassage.convertTimeToSeconds(reportRun.getTotalProcessingTime());
                    System.out.println("REFORMATTED : &&&&&" + reportRunSeconds);
                }
                reportRun.setTotalProcessingTime(String.valueOf(reportRunSeconds));
                t1.setTotalProcessingTime(String.valueOf(reportRunTwoSeconds));
                //compare the numbers
                if (reportRunSeconds > reportRunTwoSeconds) {
                    return 1;
                } else if (reportRunSeconds < reportRunTwoSeconds) {
                    return -1;
                }
                return 0;
            }
        });
    }

    private int findMinimum() {
        if (this.singleReport.size() == 0)
            return 0;
        return Integer.valueOf(this.singleReport.get(0).getTotalProcessingTime());
    }

    private int findMaximum() {
        if (this.singleReport.size() == 0)
            return 0;
        int length = this.singleReport.size();
        return Integer.valueOf(this.singleReport.get(length - 1).getTotalProcessingTime());
    }

    protected int findTotalUpTo(int index) {
        int sum = 0;
        for (int count = 0; count < index; count++) {
            sum += Integer.valueOf(this.singleReport.get(count).getTotalProcessingTime());
        }
        return sum;
    }

    private float calculateAnyTp(int tp) {
        return calulateAverageUpTo(tp);
    }

    private float calculateTp90() {
        int index = (int) Math.floor(this.singleReport.size() * 0.9);
        return calulateAverageUpTo(index);
    }

    private float calculateAverage() {
        return calulateAverageUpTo(this.singleReport.size());
    }

    private float calulateAverageUpTo(int index) {
        float sumUpTo = findTotalUpTo(index);
        float averageCal = sumUpTo / index;
        return averageCal;
    }

}
