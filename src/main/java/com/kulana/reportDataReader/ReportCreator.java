package com.kulana.reportDataReader;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

@Getter
public class ReportCreator {
    @Setter
    private int max;
    @Setter
    private int min;
    @Setter
    private float average;
    @Setter
    private float tp90;
    @Setter
    private int sum;
    @Setter
    private List<ReportRun> singleReport = new LinkedList<>();
    @Setter
    private String requestType;
    @Setter
    private int numberOfRequests;
    @Setter
    private int countOfHireRequests;
    @Setter
    private int volume;
    @Setter
    private String reportStartTime;
    @Setter
    private String reportEndTime;

    @Setter
    private int expectedBatchNumber;

    private static final int HIGH_VOLUME_NUMBEROF_BATCHES = 30;
    private static final int LOW_MIDDLE_VOLUME_NUMBEROF_BATCHES = 15;
    private static final int ANY_VOLUME_NUMBEROF_BATCHES = 30;
    private static final String formatter = "\n%-45s%-12s%-12s%-12s%-12s%-16s%-12s%-15s%-15s";

    /**
     * Printing the header only once
     */
    static {
        System.out.format("\n%-105s", "********************************************************************************" +
                "***************************************************");
        System.out.format(formatter, "RequestType", "Minimum", "Average", "Maximum",
                "No Requests", "Total_TIme", "Tp90", "#PeopleHired", "HiredPerMinute");
    }

    public void formattedOutPut() {
        System.out.format(formatter, getRequestType(), this.getMin(), this.calculateAvgBatchProcessingTIme(), this.getMax(), this.getNumberOfRequests(),
                this.getSum(), this.getTp90(), this.calculatePeopleHired(this.volume), this.getHiredPerMinute());
    }

    public ReportCreator(List<ReportRun> reportRuns, int timeDifference) {
        this(reportRuns, 0, "15");
    }

    public ReportCreator(List<ReportRun> reportRuns, int volume, String expectedBatchSize) {
        this.singleReport = reportRuns;
        //want to remove the heading part as it has no data type
        this.singleReport.remove(0);
        this.sortReportRunByResponseTIme();
        this.setVolume(volume);
        this.setAverage(calculateAverage());
        this.setMax(findMaximum());
        this.setMin(findMinimum());
        this.setTp90(calculateTp90());
        this.setSum(findTotalUpTo(this.singleReport.size()));
        this.setRequestType(getRequestTypeOfAnyReportRun());
        this.setNumberOfRequests(this.singleReport.size());
        this.setExpectedBatchNumber(Integer.valueOf(expectedBatchSize));
        this.calculatePeopleHired(getVolume());
    }

    private float getHiredPerMinute() {
        int employee = calculatePeopleHired(volume);
        return employee * 60 / this.getSum();
    }

    private String getRequestTypeOfAnyReportRun() {
        if (this.singleReport.size() >= 1) {
            return this.singleReport.get(1).getRequestType();
        }
        return "No RequestType";
    }

    public void sortReportRunByResponseTIme() {
        this.singleReport.sort(new Comparator<ReportRun>() {
            @Override
            public int compare(ReportRun reportRun, ReportRun t1) {
                int reportRunSeconds = Integer.valueOf(reportRun.getTotalProcessingTime());
                int reportRunTwoSeconds = Integer.valueOf(t1.getTotalProcessingTime());
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

    private int findTotalUpTo(int index) {
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

    private float calculateAvgBatchProcessingTIme() {
        int BATCH_SIZE = LOW_MIDDLE_VOLUME_NUMBEROF_BATCHES;
        if (this.getVolume() == VOLUME.LOW_VOLUME.volume || this.volume == VOLUME.MEDIUM_VOLUME.volume) {
            BATCH_SIZE = LOW_MIDDLE_VOLUME_NUMBEROF_BATCHES;
        } else if (this.getVolume() == VOLUME.HIGH_VOLUME.volume) {
            BATCH_SIZE = HIGH_VOLUME_NUMBEROF_BATCHES;
        } else {
            BATCH_SIZE = ANY_VOLUME_NUMBEROF_BATCHES;
        }
        return findTotalUpTo(this.singleReport.size()) / BATCH_SIZE;
    }

    private float calulateAverageUpTo(int index) {
        float sumUpTo = findTotalUpTo(index);
        float averageCal = sumUpTo / index;
        return averageCal;
    }


    public int calculatePeopleHired(int volume) {

        int SIZE = this.singleReport.size();
        if (this.singleReport.size() == 0)
            return 0;
        RequestType requestType = singleReport.get(1).getRequestTypeEnum();
        int countRequests = 0;
        if (requestType == RequestType.IMPORT_CONTRACT_CONTINGENT_WORKER) {
            if (volume == VOLUME.HIGH_VOLUME.volume) {
                countRequests = BATCH_SIZE.CONTRACTOR_HIGH.batchSize * SIZE;
                this.setCountOfHireRequests(countRequests);
            } else if (volume == VOLUME.MEDIUM_VOLUME.volume) {
                countRequests = BATCH_SIZE.CONTRACTOR_MEDIUM.batchSize * SIZE;
                this.setCountOfHireRequests(countRequests);

            } else if (volume == VOLUME.LOW_VOLUME.volume) {
                countRequests = (BATCH_SIZE.CONTRACTOR_LOW.batchSize + BATCH_SIZE.DAILY_SIZE.batchSize) * SIZE;
                this.setCountOfHireRequests(countRequests);
            } else {
                countRequests = (BATCH_SIZE.CONTRACTOR_ANY_BATCHSIZE.batchSize) * SIZE;
                this.setCountOfHireRequests(countRequests);
            }
            return getCountOfHireRequests();
        } else if (requestType == RequestType.IMPORT_CONTRACT_CONTINGENT_WORKER_DAILY || requestType == RequestType.IMPORT_HIRE_EMPLOYEE_DAILY) {
            return BATCH_SIZE.DAILY_SIZE.batchSize * SIZE;
        } else if (requestType == RequestType.IMPORT_HIRE_EMPLOYEE) {
            countRequests = 0;
            if (volume == VOLUME.HIGH_VOLUME.volume) {
                countRequests = getHiredEmployee(BATCH_SIZE.EMPLOYEE_HIGH.batchSize, BATCH_SIZE.DAILY_SIZE.batchSize);
                this.setCountOfHireRequests(countRequests);
            } else if (volume == VOLUME.MEDIUM_VOLUME.volume) {
                countRequests = getHiredEmployee(BATCH_SIZE.EMPLOYEE_MEDIUM.batchSize, BATCH_SIZE.DAILY_SIZE.batchSize);// * SIZE;
                this.setCountOfHireRequests(countRequests);

            } else if (volume == VOLUME.LOW_VOLUME.volume) {
                countRequests = getHiredEmployee(BATCH_SIZE.EMPLOYEE_LOW.batchSize, BATCH_SIZE.DAILY_SIZE.batchSize);
                this.setCountOfHireRequests(countRequests);
            } else {
                //its any volume
                countRequests = getHiredEmployee(BATCH_SIZE.EMPLOYEE_ANY_BATCHSIZE.batchSize, 5);
                this.setCountOfHireRequests(countRequests);
            }
            return getCountOfHireRequests();
        }
        return 0;
    }

    private HireEstimate getHireEstimateObject(List<ReportRun> reportRuns, int minimumBatchSize) {
        HireEstimate hireEstimate = new HireEstimate(reportRuns, minimumBatchSize);
        return hireEstimate;
    }

    private int getHiredEmployee(int minimumBatchSize, int dailySize) {
        HireEstimate hireEstimate = getHireEstimateObject(this.singleReport, minimumBatchSize);
        return hireEstimate.getCalculatedHiredEmployee(minimumBatchSize, dailySize);
    }

    class HireEstimate {
        private int minimumBatchSize;
        private ArrayList<Integer> reportRuns = new ArrayList<>();

        public HireEstimate(List<ReportRun> reportRuns, int minimumBatchSize) {
            this.reportRuns = copyResponseTIme(reportRuns);
            this.minimumBatchSize = minimumBatchSize;
        }

        private ArrayList<Integer> copyResponseTIme(List<ReportRun> reportRuns) {
            if (reportRuns == null || reportRuns.size() == 0) {
                return null;
            } else {
                reportRuns.forEach(run -> {
                    this.reportRuns.add(Integer.valueOf(run.getTotalProcessingTime()));
                });
                return this.reportRuns;
            }
        }

        public int interpolateHiredEmployee(int minimumBatchSize) {
            int SIZE = this.reportRuns.size();
            int TP10 = 10;
            int calcHiredEmp = 0;
            int batchNumber = getExpectedBatchNumber();
            float delta = (batchNumber - SIZE) / (float) batchNumber;
            double TIME_DEPENDANT_BATCH_INCREMENT_FACTOR = (1 + delta + 0.14);
            int compartment = (int) (10 * delta);
            if (getExpectedBatchNumber() == 0 || compartment <= 0) {
                return 0;
            }

            int VARIABLE_BATCH_GAP = (int) Math.ceil(SIZE / compartment);
            int i;
            int sum = 0;
            for (int varBatchCounter = 0; varBatchCounter < compartment; varBatchCounter++) {
                for (i = varBatchCounter * VARIABLE_BATCH_GAP; i < (varBatchCounter + 1) * (SIZE / compartment); i++) {
                    calcHiredEmp = minimumBatchSize * (int) Math.pow(TIME_DEPENDANT_BATCH_INCREMENT_FACTOR, varBatchCounter);
                    this.reportRuns.add(calcHiredEmp);
                    sum += calcHiredEmp;
                }
            }
            return sum;
        }

        public int getCalculatedHiredEmployee(int minimumBatchSize, int dailySize) {
            int expectedHired = getExpectedHiredEmployee(dailySize);
            if (numberOfRequests == getExpectedBatchNumber()) {
                return expectedHired;
            } else {
                return interpolateHiredEmployee(minimumBatchSize);
            }
        }

        public int getUnhiredEmployee(int minimumBatchSize) {
            return getExpectedHiredEmployee(5) - interpolateHiredEmployee(minimumBatchSize);
        }

        public int getExpectedHiredEmployee(int dailySize) {
            return numberOfRequests * (this.minimumBatchSize + dailySize);
        }

        public int getNumberOfBatches() {
            if (volume == VOLUME.HIGH_VOLUME.volume) {
                return HIGH_VOLUME_NUMBEROF_BATCHES;
            } else if (volume == VOLUME.LOW_VOLUME.volume || volume == VOLUME.MEDIUM_VOLUME.volume) {
                return LOW_MIDDLE_VOLUME_NUMBEROF_BATCHES;
            } else {
                return ANY_VOLUME_NUMBEROF_BATCHES;
            }
        }
    }

    enum BATCH_SIZE {
        CONTRACTOR_HIGH(341),
        CONTRACTOR_MEDIUM(256),
        CONTRACTOR_LOW(171),
        CONTRACTOR_ANY_BATCHSIZE(114),
        EMPLOYEE_HIGH(86),
        EMPLOYEE_MEDIUM(64),
        EMPLOYEE_LOW(43),
        EMPLOYEE_ANY_BATCHSIZE(29),
        DAILY_SIZE(5);
        int batchSize;

        BATCH_SIZE(int batchSize) {
            this.batchSize = batchSize;
        }
    }

    enum VOLUME {
        HIGH_VOLUME(100),
        MEDIUM_VOLUME(75),
        LOW_VOLUME(50),
        ANY_VOLUME(33);
        int volume;

        VOLUME(int volume) {
            this.volume = volume;
        }
    }
}
