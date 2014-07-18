package com.example.rollingball.app;

import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLDisplay;

public class MainEGLConfigChooser implements GLSurfaceView.EGLConfigChooser
{
  @Override
  public EGLConfig chooseConfig(EGL10 egl, EGLDisplay display)
  {
    int[] attributes = new int[]
      { EGL10.EGL_RED_SIZE,    8
      , EGL10.EGL_GREEN_SIZE,  8
      , EGL10.EGL_BLUE_SIZE ,  8
      , EGL10.EGL_ALPHA_SIZE,  8
      // 32 にしたかった。しかしNexus7旧型では深度バッファーは16より大きな24や32は死ぬ。深い悲しみ。
      , EGL10.EGL_DEPTH_SIZE, 16

      // GLES20 FSAA と、思ったけどNexus7旧型ではFSAA使おうとすると死ぬようだ。深い悲しみ。
      , EGL10.EGL_RENDERABLE_TYPE, 4
      //, EGL10.EGL_SAMPLE_BUFFERS, 1
      //, EGL10.EGL_SAMPLES, 2

      , EGL10.EGL_NONE
      };

    int[] number_of_config = new int[1];
    egl.eglChooseConfig( display, attributes, null, 0, number_of_config );

    EGLConfig[] configs = new EGLConfig[ number_of_config[ 0 ] ];
    egl.eglChooseConfig( display, attributes, configs, configs.length, number_of_config );

    return configs[0];
  }
}
