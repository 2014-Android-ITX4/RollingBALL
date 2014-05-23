package com.example.rollingball.app;
import java.util.*;
import java.lang.*;
import java.io.*;

/**
 * Created by sakamoto on 2014/05/14.
 */
public class SceneManager implements IUpdatable
{
  private ArrayDeque< Scene > _scenes = new ArrayDeque< Scene >();

    _scenes.addFirst();
    _scenes.addFirst();
    _scenes.addFirst();
    _scenes.addFirst();
    _scenes.addFirst();

  // MainView 参照コンストラクタ
  private MainView view;
  SceneManager( MainView view_ ){
    view = view_;
  }

  void push( Scene scene )
  {
    _scenes.push( scene );
    scene.scene_manager = this;
  }

  void replace( Scene scene )
  {
    _scenes.pop();
    _scenes.push( scene );
  }

 @Override
 public void update( final long delta_time_in_ns )
  {
    // _scenesが空のとき
    if (_scenes.isEmpty() == true)
    {
      Scene TestScene = new Scene();
      _scenes.addFirst( TestScene );

      // TODO: version-0.2.0 レベルで作成される BlandLogoScene の自動push
      //  _scenes.push(BlandLogoScene);
    }

   // bool to_exit が true ならばそのシーンをpop
  do
  {
    Scene scene = _scenes.peek();

    scene.update( delta_time_in_ns );

    if ( scene.to_exit )
    {
      _scenes.pop();
      continue;
    }
  }
  while ( false );

  }
}