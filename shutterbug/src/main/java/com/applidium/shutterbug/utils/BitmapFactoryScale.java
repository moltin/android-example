package com.applidium.shutterbug.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.InputStream;

public class BitmapFactoryScale {
    public interface InputStreamGenerator {
        public InputStream getStream();
    }

    public static Bitmap decodeSampledBitmapFromStream(InputStreamGenerator generator, DownloadRequest request) {
        if (generator == null || request == null) {
            return null;
        }
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(generator.getStream(), null, options);

            options.inSampleSize = request.getSampleSize(options);
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(generator.getStream(), null, options);




            /*FileInputStream fs=null;
            try {
                fs = (FileInputStream)generator.getStream();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFileDescriptor(((FileInputStream)generator.getStream()).getFD(), null, options);

                options.inSampleSize = request.getSampleSize(options);
                options.inJustDecodeBounds = false;

                if(fs!=null)  return BitmapFactory.decodeFileDescriptor(((FileInputStream)generator.getStream()).getFD(), null, options);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            } finally{
                if(fs!=null) {
                    try {
                        fs.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return null;*/

        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }
}
