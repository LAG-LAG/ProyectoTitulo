package com.mrswapdrobe.swapdrobe;
/*
esta clase se utiliza para obtener los nombres de las regiones y comunas provenientes del json de regiones y comunas.
 */
import android.content.Context;

import java.io.IOException;
import java.io.InputStream;

public class jsonLector {

    static String getJsonFromAssets(Context context, String fileName) {
        String jsonString;
        try {
            InputStream is = context.getAssets().open(fileName);

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            jsonString = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return jsonString;
    }
}
