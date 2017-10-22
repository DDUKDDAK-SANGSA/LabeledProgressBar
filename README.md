# LabeledProgressBar

## Description
An android progress bar which allows developers to show a label on top of the bar

## Demo
![labeledprogressbar](https://lh3.googleusercontent.com/PIcboFfU8O3cWXZtDyzNIj0d1epxVZRHL3raSk2nGmvwFeTjwk-8X9gBy3lblfMTG_qTNnhA8eytbA=w1366-h677)

## Integration

In your project build.gradle
```
allprojects {
    repositories {
        ...
        maven {
            url  "https://dl.bintray.com/giangp2901/android"
        }
    }
}
```

In your app build.gradle

```
dependencies {
    ...
    compile 'com.github.giangpham96:labeledprogressbarlib:1.0'
    ...
}
```

## Usage
```
<leo.me.la.labeledprogressbarlib.LabeledProgressBar
        android:id="@+id/pb"
        android:layout_width="150dp"
        android:layout_height="wrap_content"
        android:layout_margin="16dp"
        app:indeterminate="false"
        app:labelBackgroundColor="#f9f37e"
        app:labelText="giangp"
        app:labelValueType="percentage"
        app:maxValue="100"
        app:minValue="0"
        app:progressBeginColor="#029bac"
        app:progressCompleteColor="#8121ff"
        app:progressHeight="15dp"
        app:textColor="#b90000"
        app:value="0" />
  ```
  
  ### Properties
  * **indeterminate** - if set to true, then the label is hidden
  * **labelBackgroundColor**
  * **labelText** - the text displayed on the label when labelValueType is set to custom 
  * **labelValueType** - must be set to one of these values: percentage, value, custom
  * **maxValue**
  * **minValue**
  * **progressBeginColor**
  * **progressCompleteColor**
  * **textColor**
  * **value**
  
  The progress bar's color will gradually change from **progressBeginColor** to **progressCompleteColor** as it progresses from **minValue** to **maxValue**
  
## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details
