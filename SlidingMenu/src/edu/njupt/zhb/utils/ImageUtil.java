package edu.njupt.zhb.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.Typeface;

import edu.njupt.zhb.slidemenu.R;

public class ImageUtil {
	
	
	public static Bitmap ImgIcon(Context context,String code)
	 {
		int resID = context.getResources().getIdentifier(code, "drawable",
					context.getPackageName());
		 Bitmap photo= BitmapFactory.decodeResource(context.getResources(),
				 resID);
		  return photo;
	 }
	public static Bitmap drawText(Context context,String code,float fontSize,int x,int y,String textName){
		Bitmap photo =ImgIcon(context,code);
		int width = photo.getWidth(), hight = photo.getHeight();
		return drawText(context, photo, fontSize,width/2-x,hight-y, textName);
	}
	public static Bitmap drawText(Context context,int imageId,float fontSize,int x,int y,String textName){
		Bitmap photo = BitmapFactory.decodeResource(context.getResources(),
				imageId);//mImageIds[position]);
		int width = photo.getWidth(), hight = photo.getHeight();
		return drawText(context, photo, fontSize,width/2-x,hight-y, textName);
	}
	public static Bitmap drawText(Context context,Bitmap photo,float fontSize,int x,int y,String textName){
		int width = photo.getWidth(), hight = photo.getHeight();
		//System.out.println("��" + width + "��" + hight);
		Bitmap icon = Bitmap.createBitmap(width, hight,
				Bitmap.Config.ARGB_8888); // ����һ���յ�BItMap
		Canvas canvas = new Canvas(icon);// ��ʼ������ ���Ƶ�ͼ��icon��

		Paint photoPaint = new Paint(); // ��������
		photoPaint.setDither(true); // ��ȡ�������ͼ�����
		photoPaint.setFilterBitmap(true);// ����һЩ

		Rect src = new Rect(0, 0, photo.getWidth(), photo.getHeight());// ����һ��ָ�����¾��ε����
		Rect dst = new Rect(0, 0, width, hight);// ����һ��ָ�����¾��ε����
		canvas.drawBitmap(photo, src, dst, photoPaint);// ��photo ���Ż�������
														// dstʹ�õ������photoPaint

		Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG
				| Paint.DEV_KERN_TEXT_FLAG);// ���û���
		textPaint.setTextSize(fontSize);// �����С
		// textPaint.setTypeface(Typeface.MONOSPACE);//����Ĭ�ϵĿ��
		textPaint.setColor(Color.WHITE);// ���õ���ɫ
		textPaint.setTextAlign(Align.CENTER);
		Typeface font = Typeface.create(Typeface.MONOSPACE, Typeface.BOLD);
		textPaint.setTypeface(font);
		textPaint.setShadowLayer(3f, 1, 1,
				context.getResources().getColor(android.R.color.background_dark));// Ӱ��������
		canvas.drawText(textName, x,y, textPaint);// ������ȥ
																			// �֣���ʼδ֪x,y������ֻ�ʻ���
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		
		return icon;
	}
}
