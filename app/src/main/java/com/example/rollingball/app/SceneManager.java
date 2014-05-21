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

  public static void main( String[] args ) throws java.lang.Exception
  {
    _scenes.addScene(  );
    _scenes.addScene(  );
    _scenes.addScene(  );
    _scenes.addScene(  );
    _scenes.addScene(  );
  }

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

  void update( Scene scene )
  {
    scene = _scenes.peek();
    update( scene );

    //シーンの bool to_exit が true ならばそのシーンをpopする
    boolean to_exit  = false;
    if (scene == exit){
      to_exit = true;
    }
    if (to_exit == true)
    {
      _scenes.pop();
      update( scene );
    }

    if ( _scenes == null )
    {
      Scene TestScene = new TestScene;
      _scenes.push( TestScene );
      // TODO: version-0.2.0 レベルで作成される BlandLogoScene の自動push
      _scenes.push(BlandLogoScene);
    }
  }
}