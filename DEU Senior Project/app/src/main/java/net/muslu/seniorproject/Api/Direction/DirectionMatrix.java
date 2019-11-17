package net.muslu.seniorproject.Api.Direction;

import android.os.AsyncTask;

import net.muslu.seniorproject.Reader.Barcode.BarcodeData;

public class DirectionMatrix {
    protected BarcodeData barcodeData;
    public BarcodeData getBarcodeData() {
        return barcodeData;
    }

    public void setBarcodeData(BarcodeData barcodeData) {
        this.barcodeData = barcodeData;
    }

    public DirectionMatrix(BarcodeData barcodeData){
        this.setBarcodeData(barcodeData);
    }

    protected class GetDataAsync extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }

}
