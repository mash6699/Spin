/*
package mx.spin.mobile.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.pdf.PrintedPdfDocument;
import android.util.Log;

import mx.spin.mobile.entitys.InfoAnalisisPDF;
import mx.spin.mobile.entitys.ItemMantenimiento;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;



*/
/**
 * Created by robe on 20/02/16.
 *//*

public class MyPrintDocumentAdapter extends PrintDocumentAdapter
{
    Context context;
    private int pageHeight;
    private int pageWidth;
    public PdfDocument myPdfDocument;
    public int totalpages = 1;
    private String titulo;
    private String textoMantenimientoRutinario;
    private ArrayList<ItemMantenimiento> listadoMantenimiento;

    public MyPrintDocumentAdapter(Context context)
    {
        this.context = context;
    }

    @Override
    public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, android.os.CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle extras) {
        myPdfDocument = new PrintedPdfDocument(context, newAttributes);

        pageHeight = newAttributes.getMediaSize().getHeightMils()/1000 * 72;
        pageWidth = newAttributes.getMediaSize().getWidthMils()/1000 * 72;

        if (cancellationSignal.isCanceled() ) {
            callback.onLayoutCancelled();
            return;
        }

        if (totalpages > 0) {
            Random r = new Random();
            Integer random = r.nextInt(999);
            String nombre = "analisis_piscina"+random+".pdf";
            PrintDocumentInfo.Builder builder = new PrintDocumentInfo
                    .Builder(nombre)
                    .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .setPageCount(totalpages);

            PrintDocumentInfo info = builder.build();
            Realm realm = Realm.getInstance(context);
            realm.beginTransaction();
            RealmResults<InfoAnalisisPDF> list = realm.where(InfoAnalisisPDF.class).findAll();
            list.clear();
            realm.commitTransaction();

            realm.beginTransaction();
            InfoAnalisisPDF doc = realm.createObject(InfoAnalisisPDF.class);
            doc.setNombre(info.getName());
            realm.commitTransaction();

            callback.onLayoutFinished(info, true);
            Log.d("PDF",info.getName());
        } else {
            callback.onLayoutFailed("Page count is zero.");
        }
    }

    @Override
    public void onWrite(PageRange[] pageRanges, ParcelFileDescriptor destination, android.os.CancellationSignal cancellationSignal, WriteResultCallback callback) {
        for (int i = 0; i < totalpages; i++) {
            if (pageInRange(pageRanges, i))
            {
                PdfDocument.PageInfo newPage = new PdfDocument.PageInfo.Builder(pageWidth,
                        pageHeight, i).create();

                PdfDocument.Page page =
                        myPdfDocument.startPage(newPage);

                if (cancellationSignal.isCanceled()) {
                    callback.onWriteCancelled();
                    myPdfDocument.close();
                    myPdfDocument = null;
                    return;
                }
                drawPage(page, i);

                myPdfDocument.finishPage(page);
            }
        }

        try {
            myPdfDocument.writeTo(new FileOutputStream(
                    destination.getFileDescriptor()));
        } catch (IOException e) {
            callback.onWriteFailed(e.toString());
            return;
        } finally {
            myPdfDocument.close();
            myPdfDocument = null;
        }

        callback.onWriteFinished(pageRanges);
    }
    private boolean pageInRange(PageRange[] pageRanges, int page)
    {
        for (int i = 0; i<pageRanges.length; i++)
        {
            if ((page >= pageRanges[i].getStart()) &&
                    (page <= pageRanges[i].getEnd()))
                return true;
        }
        return false;
    }

    private void drawPage(PdfDocument.Page page,
                          int pagenumber) {
        Canvas canvas = page.getCanvas();

        pagenumber++; // Make sure page numbers start at 1

        int titleBaseLine = 72;
        int leftMargin = 54;

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
       //titulo
        paint.setTextSize(40);
        canvas.drawText(getTitulo(), leftMargin, titleBaseLine, paint);
        //fecha
        paint.setTextSize(30);
        Date date = new Date(2016);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        canvas.drawText(df.format(date).toString(), leftMargin,titleBaseLine+35,paint);
        //mantenimiento
        canvas.drawText("Mantenimiento rutinario", leftMargin,titleBaseLine+75,paint);
        paint.setTextSize(25);
        canvas.drawText(getTextoMantenimientoRutinario(), leftMargin, titleBaseLine + 105, paint);

        paint.setTextSize(20);
        canvas.drawText("Aplicacion de quimico", leftMargin, titleBaseLine + 135, paint);

        if (pagenumber % 2 == 0)
            paint.setColor(Color.RED);
        else
            paint.setColor(Color.GREEN);

       // PdfDocument.PageInfo pageInfo = page.getInfo();
//        canvas.drawCircle(pageInfo.getPageWidth()/2,
//                pageInfo.getPageHeight()/2,
//                150,
//                paint);
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getTextoMantenimientoRutinario() {
        return textoMantenimientoRutinario;
    }

    public void setTextoMantenimientoRutinario(String textoMantenimientoRutinario) {
        this.textoMantenimientoRutinario = textoMantenimientoRutinario;
    }

    public ArrayList<ItemMantenimiento> getListadoMantenimiento() {
        return listadoMantenimiento;
    }

    public void setListadoMantenimiento(ArrayList<ItemMantenimiento> listadoMantenimiento) {
        this.listadoMantenimiento = listadoMantenimiento;
    }
}*/
