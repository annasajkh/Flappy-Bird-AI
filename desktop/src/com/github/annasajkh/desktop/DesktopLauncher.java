package com.github.annasajkh.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.github.annasajkh.Core;

public class DesktopLauncher
{
    public static void main(String[] arg)
    {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = 416;
        config.height = 600;
        config.title = "Flappy Bird AI";
        new LwjglApplication(new Core(), config);
    }
}
