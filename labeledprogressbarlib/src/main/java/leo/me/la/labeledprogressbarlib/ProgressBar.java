package leo.me.la.labeledprogressbarlib;

interface ProgressBar {

    void setLabelValueType(LabeledProgressBar.LabelValueType type);

    /**
     * @param value the text that will be display on the label
     *              when the progressLabelValueType is CUSTOM
     */
    void setLabelText(String value);

    void setLabelBackgroundColor(int color);

    void setTextColor(int color);

    /**
     * this method will make the progress bar indeterminate
     * and hide the label if indeterminate is true. Otherwise, it
     * will show the label
     * @param indeterminate the state of the bar
     */
    void setIndeterminate(boolean indeterminate);

    int getValue();

    void setValue(int value);

    int getMinValue();

    void setMinValue(int minValue);

    int getMaxValue();

    void setMaxValue(int maxValue);

    void setProgressStartColor(int color);

    void setProgressCompleteColor(int color);

    void animateToValue(int value, long duration);
}
