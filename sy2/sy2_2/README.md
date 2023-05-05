# 实验2_2_构建CameraX应用

1. 掌 握 Android CameraX 拍照功能的基本 用 法 。
2. 掌握Android CameraX 视频捕捉功能的基本用法。
3. 进一步熟悉Kotlin语言的特性

------

[TOC]

------

### 1.创建项目

首先创建一个“Empty Activity”新项目。

![2.png](./Images/2.png)



将项目命名为“**CameraXApp**”，软件包名称更改为“**com.android.example.cameraxapp**”。选择**Kotlin**语言开发，设定最低支持的**API Level 21**

![1.png](./Images/1.png)



### 2.添加 Gradle 依赖

打开项目的模块（Module）的build.gradle 文件，并添加 CameraX 依赖项：

```kotlin
dependencies {
    def camerax_version = "1.1.0-beta01"
    implementation "androidx.camera:camera-core:${camerax_version}"
    implementation "androidx.camera:camera-camera2:${camerax_version}"
    implementation "androidx.camera:camera-lifecycle:${camerax_version}"
    implementation "androidx.camera:camera-video:${camerax_version}"

    implementation "androidx.camera:camera-view:${camerax_version}"
    implementation "androidx.camera:camera-extensions:${camerax_version}"
}
```

为正常使用ViewBinding在 android{} 代码块末尾添加如下代码：

```kotlin
buildFeatures {
   viewBinding true
}
```

点击 **Sync Now**，更新相关依赖库。



### 3.创建项目布局

打开res/layout/activity_main.xml 的 activity_main 布局文件，并将其替换为以下代码：

```kotlin
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout
   xmlns:android="http://schemas.android.com/apk/res/android"
   xmlns:app="http://schemas.android.com/apk/res-auto"
   xmlns:tools="http://schemas.android.com/tools"
   android:layout_width="match_parent"
   android:layout_height="match_parent"
   tools:context=".MainActivity">

   <androidx.camera.view.PreviewView
       android:id="@+id/viewFinder"
       android:layout_width="match_parent"
       android:layout_height="match_parent" />

   <Button
       android:id="@+id/image_capture_button"
       android:layout_width="110dp"
       android:layout_height="110dp"
       android:layout_marginBottom="50dp"
       android:layout_marginEnd="50dp"
       android:elevation="2dp"
       android:text="@string/take_photo"
       app:layout_constraintBottom_toBottomOf="parent"
       app:layout_constraintLeft_toLeftOf="parent"
       app:layout_constraintEnd_toStartOf="@id/vertical_centerline" />

   <Button
       android:id="@+id/video_capture_button"
       android:layout_width="110dp"
       android:layout_height="110dp"
       android:layout_marginBottom="50dp"
       android:layout_marginStart="50dp"
       android:elevation="2dp"
       android:text="@string/start_capture"
       app:layout_constraintBottom_toBottomOf="parent"
       app:layout_constraintStart_toEndOf="@id/vertical_centerline" />

   <androidx.constraintlayout.widget.Guideline
       android:id="@+id/vertical_centerline"
       android:layout_width="wrap_content"
       android:layout_height="wrap_content"
       android:orientation="vertical"
       app:layout_constraintGuide_percent=".50" />

</androidx.constraintlayout.widget.ConstraintLayout>

```

更新res/values/strings.xml 文件:

```kotlin
<resources>
   <string name="app_name">CameraXApp</string>
   <string name="take_photo">Take Photo</string>
   <string name="start_capture">Start Capture</string>
   <string name="stop_capture">Stop Capture</string>
</resources>

```

更新完后，界面如下：

![3.png](./Images/3.png)



### 4.编写 MainActivity.kt 代码

将 MainActivity.kt 中的代码替换为以下代码：

```kotlin
package com.android.example.cameraxapp

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageCapture
import androidx.camera.video.Recorder
import androidx.camera.video.Recording
import androidx.camera.video.VideoCapture
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.example.cameraxapp.databinding.ActivityMainBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import android.widget.Toast
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.core.Preview
import androidx.camera.core.CameraSelector
import android.util.Log
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.video.FallbackStrategy
import androidx.camera.video.MediaStoreOutputOptions
import androidx.camera.video.Quality
import androidx.camera.video.QualitySelector
import androidx.camera.video.VideoRecordEvent
import androidx.core.content.PermissionChecker
import java.nio.ByteBuffer
import java.text.SimpleDateFormat
import java.util.Locale

typealias LumaListener = (luma: Double) -> Unit

class MainActivity : AppCompatActivity() {
   private lateinit var viewBinding: ActivityMainBinding

   private var imageCapture: ImageCapture? = null

   private var videoCapture: VideoCapture<Recorder>? = null
   private var recording: Recording? = null

   private lateinit var cameraExecutor: ExecutorService

   override fun onCreate(savedInstanceState: Bundle?) {
       super.onCreate(savedInstanceState)
       viewBinding = ActivityMainBinding.inflate(layoutInflater)
       setContentView(viewBinding.root)

       // Request camera permissions
       if (allPermissionsGranted()) {
           startCamera()
       } else {
           ActivityCompat.requestPermissions(
               this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
       }

       // Set up the listeners for take photo and video capture buttons
       viewBinding.imageCaptureButton.setOnClickListener { takePhoto() }
       viewBinding.videoCaptureButton.setOnClickListener { captureVideo() }

       cameraExecutor = Executors.newSingleThreadExecutor()
   }

   private fun takePhoto() {}

   private fun captureVideo() {}

   private fun startCamera() {}

   private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
       ContextCompat.checkSelfPermission(
           baseContext, it) == PackageManager.PERMISSION_GRANTED
   }

   override fun onDestroy() {
       super.onDestroy()
       cameraExecutor.shutdown()
   }

   companion object {
       private const val TAG = "CameraXApp"
       private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
       private const val REQUEST_CODE_PERMISSIONS = 10
       private val REQUIRED_PERMISSIONS =
           mutableListOf (
               Manifest.permission.CAMERA,
               Manifest.permission.RECORD_AUDIO
           ).apply {
               if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                   add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
               }
           }.toTypedArray()
   }
}

```

此时系统已实现 onCreate()，供我们检查相机权限、启动相机、为照片和拍摄按钮设置 onClickListener()，以及实现 cameraExecutor。



### 5.请求必要的权限

**目的：**

应用需要获得用户授权才能打开相机；录制音频也需要麦克风权限；MediaStore 需要外部存储空间写入权限。在此步骤中，我们将实现这些必要的权限。

**步骤：**

1. 打开 AndroidManifest.xml，然后将以下代码行添加到 application 标记之前。

   ```kotlin
   <uses-feature android:name="android.hardware.camera.any" />
   <uses-permission android:name="android.permission.CAMERA" />
   <uses-permission android:name="android.permission.RECORD_AUDIO" />
   <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
      android:maxSdkVersion="28" />
   ```

   

2. 在MainActivity.kt中添加如下代码：

   ```kotlin
   override fun onRequestPermissionsResult(
      requestCode: Int, permissions: Array<String>, grantResults:
      IntArray) {
      if (requestCode == REQUEST_CODE_PERMISSIONS) {
          if (allPermissionsGranted()) {
              startCamera()
          } else {
              Toast.makeText(this,
                  "Permissions not granted by the user.",
                  Toast.LENGTH_SHORT).show()
              finish()
          }
      }
   }
   
   ```

**运行结果：**应用程序成功请求使用摄像头和麦克风权限

![4.png](./Images/4.png)



但打开相机无法实时预览场景，相机仍无法正常工作。

![5.png](./Images/5.png)



### 6.实现 Preview 用例

**目的：**实现相机取景器预览效果

**步骤：**

1. 填充之前的startCamera() 函数：

   ```kotlin
   private fun startCamera() {
      val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
   
      cameraProviderFuture.addListener({
          // Used to bind the lifecycle of cameras to the lifecycle owner
          val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
   
          // Preview
          val preview = Preview.Builder()
             .build()
             .also {
                 it.setSurfaceProvider(viewBinding.viewFinder.surfaceProvider)
             }
   
          // Select back camera as a default
          val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
   
          try {
              // Unbind use cases before rebinding
              cameraProvider.unbindAll()
   
              // Bind use cases to camera
              cameraProvider.bindToLifecycle(
                  this, cameraSelector, preview)
   
          } catch(exc: Exception) {
              Log.e(TAG, "Use case binding failed", exc)
          }
   
      }, ContextCompat.getMainExecutor(this))
   }
   
   ```

**运行结果：**成功获得预览效果。

![6.png](./Images/6.png)



### 7.实现 ImageCapture 用例

**目的：**实现拍照功能

**步骤：**

1. 填充takePhoto() 方法，代码如下：

   ```kotlin
   private fun takePhoto() {
      // Get a stable reference of the modifiable image capture use case
      val imageCapture = imageCapture ?: return
   
      // Create time stamped name and MediaStore entry.
      val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
                 .format(System.currentTimeMillis())
      val contentValues = ContentValues().apply {
          put(MediaStore.MediaColumns.DISPLAY_NAME, name)
          put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
          if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
              put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/CameraX-Image")
          }
      }
   
      // Create output options object which contains file + metadata
      val outputOptions = ImageCapture.OutputFileOptions
              .Builder(contentResolver,
                       MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                       contentValues)
              .build()
   
      // Set up image capture listener, which is triggered after photo has
      // been taken
      imageCapture.takePicture(
          outputOptions,
          ContextCompat.getMainExecutor(this),
          object : ImageCapture.OnImageSavedCallback {
              override fun onError(exc: ImageCaptureException) {
                  Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
              }
   
              override fun
                  onImageSaved(output: ImageCapture.OutputFileResults){
                  val msg = "Photo capture succeeded: ${output.savedUri}"
                  Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
                  Log.d(TAG, msg)
              }
          }
      )
   }
   
   ```

2. 找到 startCamera() 方法，并将下面代码复制到要预览的代码下方：

   ```kotlin
   imageCapture = ImageCapture.Builder().build()
   ```

3. 在 try 代码块中更新对 bindToLifecycle() 的调用，以包含新的用例：

   ```kotlin
   cameraProvider.bindToLifecycle(
      this, cameraSelector, preview, imageCapture)
   ```



最终，startCamera() 代码如下：

```kotlin
private fun startCamera() {
    val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

    cameraProviderFuture.addListener({
        // Used to bind the lifecycle of cameras to the lifecycle owner
        val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

        // Preview
        val preview = Preview.Builder()
            .build()
            .also {
                it.setSurfaceProvider(viewBinding.viewFinder.surfaceProvider)
            }
        imageCapture = ImageCapture.Builder().build()

        // Select back camera as a default
        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

        try {
            // Unbind use cases before rebinding
            cameraProvider.unbindAll()

            // Bind use cases to camera
            cameraProvider.bindToLifecycle(
                this, cameraSelector, preview, imageCapture)


        } catch(exc: Exception) {
            Log.e(TAG, "Use case binding failed", exc)
        }

    }, ContextCompat.getMainExecutor(this))
}
```

在点击拍照时出现问题：

![7.png](./Images/7.jpg)

**解决方案：**换一个API 30（API 31 不行）的模拟器

**运行结果：**

![8.png](./Images/8.png)



同时在相册也能查看到拍摄的照片：

![9.png](./Images/9.png)



### 8.实现 ImageAnalysis 用例

**目的：**

丰富相机功能，添加一个可以分析图像平均亮度的分析器。

**步骤：**

1.  在MainActivity.kt中添加如下代码 ：

   ```kotlin
   private class LuminosityAnalyzer(private val listener: LumaListener) : ImageAnalysis.Analyzer {
   
      private fun ByteBuffer.toByteArray(): ByteArray {
          rewind()    // Rewind the buffer to zero
          val data = ByteArray(remaining())
          get(data)   // Copy the buffer into a byte array
          return data // Return the byte array
      }
   
      override fun analyze(image: ImageProxy) {
   
          val buffer = image.planes[0].buffer
          val data = buffer.toByteArray()
          val pixels = data.map { it.toInt() and 0xFF }
          val luma = pixels.average()
   
          listener(luma)
   
          image.close()
      }
   }
   
   ```

2. 更新startCamera()，将以下代码添加到 imageCapture 代码下方

   ```kotlin
   val imageAnalyzer = ImageAnalysis.Builder()
      .build()
      .also {
          it.setAnalyzer(cameraExecutor, LuminosityAnalyzer { luma ->
              Log.d(TAG, "Average luminosity: $luma")
          })
      }
   
   ```

   

3. 更新 cameraProvider 上的 bindToLifecycle() 调用，以包含 imageAnalyzer。

   ```kotlin
   cameraProvider.bindToLifecycle(
      this, cameraSelector, preview, imageCapture, imageAnalyzer)
   ```

最终，startCamera()完整代码如下：

```kotlin
private fun startCamera() {
   val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

   cameraProviderFuture.addListener({
       // Used to bind the lifecycle of cameras to the lifecycle owner
       val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

       // Preview
       val preview = Preview.Builder()
           .build()
           .also {
               it.setSurfaceProvider(viewBinding.viewFinder.surfaceProvider)
           }

       imageCapture = ImageCapture.Builder()
           .build()

       val imageAnalyzer = ImageAnalysis.Builder()
           .build()
           .also {
               it.setAnalyzer(cameraExecutor, LuminosityAnalyzer { luma ->
                   Log.d(TAG, "Average luminosity: $luma")
               })
           }

       // Select back camera as a default
       val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

       try {
           // Unbind use cases before rebinding
           cameraProvider.unbindAll()

           // Bind use cases to camera
           cameraProvider.bindToLifecycle(
               this, cameraSelector, preview, imageCapture, imageAnalyzer)

       } catch(exc: Exception) {
           Log.e(TAG, "Use case binding failed", exc)
       }

   }, ContextCompat.getMainExecutor(this))
}

```

**运行结果：**

![10.png](./Images/10.png)



### 9.实现 VideoCapture 用例

**目的：**实现视频拍摄功能

**步骤：**

1. 将以下代码复制到captureVideo() 方法：

   ```kotlin
   // Implements VideoCapture use case, including start and stop capturing.
   private fun captureVideo() {
      val videoCapture = this.videoCapture ?: return
   
      viewBinding.videoCaptureButton.isEnabled = false
   
      val curRecording = recording
      if (curRecording != null) {
          // Stop the current recording session.
          curRecording.stop()
          recording = null
          return
      }
   
      // create and start a new recording session
      val name = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
                 .format(System.currentTimeMillis())
      val contentValues = ContentValues().apply {
          put(MediaStore.MediaColumns.DISPLAY_NAME, name)
          put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4")
          if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
              put(MediaStore.Video.Media.RELATIVE_PATH, "Movies/CameraX-Video")
          }
      }
   
      val mediaStoreOutputOptions = MediaStoreOutputOptions
          .Builder(contentResolver, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
          .setContentValues(contentValues)
          .build()
      recording = videoCapture.output
          .prepareRecording(this, mediaStoreOutputOptions)
          .apply {
              if (PermissionChecker.checkSelfPermission(this@MainActivity,
                      Manifest.permission.RECORD_AUDIO) ==
                  PermissionChecker.PERMISSION_GRANTED)
              {
                  withAudioEnabled()
              }
          }
          .start(ContextCompat.getMainExecutor(this)) { recordEvent ->
              when(recordEvent) {
                  is VideoRecordEvent.Start -> {
                      viewBinding.videoCaptureButton.apply {
                          text = getString(R.string.stop_capture)
                          isEnabled = true
                      }
                  }
                  is VideoRecordEvent.Finalize -> {
                      if (!recordEvent.hasError()) {
                          val msg = "Video capture succeeded: " +
                              "${recordEvent.outputResults.outputUri}"
                          Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT)
                               .show()
                          Log.d(TAG, msg)
                      } else {
                          recording?.close()
                          recording = null
                          Log.e(TAG, "Video capture ends with error: " +
                              "${recordEvent.error}")
                      }
                      viewBinding.videoCaptureButton.apply {
                          text = getString(R.string.start_capture)
                          isEnabled = true
                      }
                  }
              }
          }
   }
   
   ```

   

2. 在 startCamera() 中，将以下代码放置在 preview 创建行之后。

   ```kotlin
   val recorder = Recorder.Builder()
      .setQualitySelector(QualitySelector.from(Quality.HIGHEST))
      .build()
   videoCapture = VideoCapture.withOutput(recorder)
   
   ```

   

3. 将 cameraProvider.bindToLifecycle() 调用替换为以下代码：

   ```kotlin
   cameraProvider.bindToLifecycle(this, cameraSelector, preview, videoCapture)
   ```

4. 构建并运行项目

**运行结果：**

![11.png](./Images/11.png)



可以在相册中查看录制的视频：

![12.png](./Images/12.png)



