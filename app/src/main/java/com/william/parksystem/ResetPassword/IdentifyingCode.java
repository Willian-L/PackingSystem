package com.william.parksystem.ResetPassword;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.Random;

public class IdentifyingCode {

    private static IdentifyingCode IdentifyingCode;

    private static final char[] CHARS = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    };

    public static IdentifyingCode getInstance() {
        if (IdentifyingCode == null) {
            IdentifyingCode = new IdentifyingCode();
        }
        return IdentifyingCode;
    }

    private static final int CODE_LENGTH = 4;
    private static final int FONT_SIZE = 50;
    private static final int LINE_NUMBER = 5;
    private static final int BASE_PADDING_LEFT = 10, RANGE_PADDING_LEFT = 100, BASE_PADDING_TOP = 75, RANGE_PADDING_TOP = 50;
    private static final int DEFAULT_WIDTH = 400, DEFAULT_HEIGHT = 150;
    private int width = DEFAULT_WIDTH, height = DEFAULT_HEIGHT;
    private int base_padding_left = BASE_PADDING_LEFT, range_padding_left = RANGE_PADDING_LEFT,
            base_padding_top = BASE_PADDING_TOP, range_padding_top = RANGE_PADDING_TOP;
    private int codeLength = CODE_LENGTH, line_number = LINE_NUMBER, font_size = FONT_SIZE;

    private String code;
    private int padding_left, padding_top;
    private Random random = new Random();

    public Bitmap createBitmap() {
        padding_left = 0;
        padding_top = 0;
        Bitmap bp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bp);

        code = createCode();
        c.drawColor(Color.WHITE);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextSize(font_size);
//        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        for (int i = 0; i < code.length(); i++) {
            randomTextStyle(paint);
            randomPadding();
            c.drawText(code.charAt(i) + "", padding_left, padding_top, paint);
        }
        for (int i = 0; i < line_number; i++) {
            drawLine(c, paint);
        }
        c.save();
        c.restore();
        return bp;
    }

    private String createCode() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < codeLength; i++) {
            sb.append(CHARS[random.nextInt(CHARS.length)]);
        }
        return sb.toString();
    }

    private void drawLine(Canvas canvas, Paint paint) {
        int color = randomColor();
        int startX = random.nextInt(width);
        int startY = random.nextInt(height);
        int stopX = random.nextInt(width);
        int stopY = random.nextInt(height);
        paint.setStrokeWidth(1);
        paint.setColor(color);
        canvas.drawLine(startX, startY, stopX, stopY, paint);
    }

    private void randomTextStyle(Paint paint) {
        int color = randomColor();
        paint.setColor(color);
        paint.setFakeBoldText(random.nextBoolean());
        double skew = random.nextInt(11) / 10;
        skew = random.nextBoolean() ? skew : -skew;
    }

    private int randomColor() {
        return randomColor(1);
    }

    private int randomColor(int rate) {
        int red = random.nextInt(256) / rate;
        int green = random.nextInt(256) / rate;
        int blue = random.nextInt(256) / rate;
        return Color.rgb(red, green, blue);
    }

    private void randomPadding() {

        padding_left += base_padding_left + random.nextInt(range_padding_left);
        padding_top = base_padding_top + random.nextInt(range_padding_top);
    }

    public String getCode() {
        return code;
    }
}
