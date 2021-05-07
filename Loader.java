package com.coringa.modz;
 
import android.app.Activity;
import android.os.Bundle;
import dalvik.system.DexClassLoader;
import android.content.Intent;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.lang.ref.WeakReference;

public class MainActivity extends Activity
{
    public void onCreate(Bundle savedInstanceState)
    {
        ClassLoader TEKASHITEAM = new DexClassLoader("/storage/emulated/0/CORINGA LOADER DEX ASSETS/classes_tusar.jar", getCacheDir().getAbsolutePath(), null, getClassLoader());
        setAPKClassLoader(TEKASHITEAM);

        try {
            Class<?> activityClass = TEKASHITEAM.loadClass("com.coringa.modz.MainActivity");
            Intent intent = new Intent(this, activityClass);
            startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        finish();
    }

    private void setAPKClassLoader(ClassLoader classLoader)
    {
        try {
            Field mMainThread = getField(Activity.class, "mMainThread");
            Object mainThread = mMainThread.get(this);
            Class threadClass = mainThread.getClass();
            Field mPackages = getField(threadClass, "mPackages");
            HashMap<String,?> map = (HashMap<String,?>) mPackages.get(mainThread);
            WeakReference<?> ref = (WeakReference<?>) map.get(getPackageName());
            Object apk = ref.get();
            Class apkClass = apk.getClass();
            Field mClassLoader = getField(apkClass, "mClassLoader");
            mClassLoader.set(apk, classLoader);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private Field getField(Class<?> cls, String name)
    {
        for (Field field: cls.getDeclaredFields())
        {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            if (field.getName().equals(name)) {
                return field;
            }
        }
        return null;
    }

}
