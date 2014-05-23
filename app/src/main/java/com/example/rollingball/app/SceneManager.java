package com.example.rollingball.app;
import java.util.*;
import java.lang.*;
import java.io.*;

public class SceneManager implements IUpdatable, IDrawable
{
  private ArrayDeque< Scene > _scenes = new ArrayDeque< Scene >();

  private MainView view;

  // MainView 参照コンストラクタ
  SceneManager( final MainView view_ )
  { view = view_; }

  void push( Scene scene )
  {
    scene.scene_manager = this;
    _scenes.push( scene );
  }

  void replace( Scene scene )
  {
    _scenes.pop();
    push( scene );
  }

  @Override
  public void update( final long delta_time_in_ns )
  {
    do
    {
      Scene scene = _scenes.peek();

      scene.update( delta_time_in_ns );

      // bool to_exit が true ならばそのシーンをpop
      if ( scene.to_exit )
      {
        _scenes.pop();

        // _scenesが空のとき
        if ( _scenes.isEmpty() == true )
        {
          // TODO: version-0.2.0 レベルで作成される BlandLogoScene の自動push
          // push( new BlandLogoScene(  ) );
          push( new TestScene( this ) );

        }

        continue;
      }
    }
    while ( false );

  }

  @Override
  public void draw()
  { _scenes.peek().draw(); }

}