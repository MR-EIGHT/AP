package com.eight.num;


import java.util.Arrays;


public class BigNum {

    public static final BigNum ZERO = new BigNum(0);
    public static final BigNum ONE = new BigNum(1);

    private final byte[] digits;


    private String clean(String s) {
        s = s.trim();
        int counter = 0;
        for (int i = 0; i < s.length(); i++)
            if (s.charAt(i) == '0')
                counter++;
        if (counter == s.length()) return "0";

        StringBuilder digits = new StringBuilder(s.length());
        boolean leadingZero = true;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) != '0')
                leadingZero = false;
            if (leadingZero && s.charAt(i) == '0')
                continue;
            digits.append(s.charAt(i));
        }
        return digits.toString();


    }


    public BigNum(String str) {
        str = clean(str);
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) < '0' || str.charAt(i) > '9')
                throw new IllegalArgumentException("Bad input: " + str);
        }

        digits = new byte[str.length()];
        for (int i = 0; i < digits.length; i++) {
            int ch = (str.charAt(str.length() - i - 1) - 48);
            digits[i] = (byte) ch;
        }
    }

    public BigNum(long val) {
        this(Long.toString(val));
    }

    public BigNum() {
        this(0);
    }

    public static BigNum fromLong(long val) {
        return new BigNum((val));
    }

    public static BigNum fromString(String val) {
        return new BigNum((val));
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder((digits.length));
        for (byte digit : digits) {
            builder.append(digit);
        }
        return builder.reverse().toString();

    }

    int length() {
        return digits.length;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BigNum bigNum = (BigNum) o;
        return Arrays.equals(digits, bigNum.digits);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(digits);
    }

    boolean isGreaterThan(BigNum a) {
        if (length() > a.length())
            return true;
        if (length() < a.length())
            return false;
        for (int i = digits.length - 1; i >= 0; i--) {
            if (digits[i] > a.digits[i])
                return true;
            if (digits[i] < a.digits[i])
                return false;

        }
        return false;
    }

   protected int compareTo(BigNum a) {
        if (length() > a.length())
            return 1;
        if (length() < a.length())
            return -1;
        for (int i = digits.length - 1; i >= 0; i--) {
            if (digits[i] > a.digits[i])
                return 1;
            if (digits[i] < a.digits[i])
                return -1;

        }
        return 0;
    }


    public static int max(int a, int b) {
        if (a >= b) return a;
        else return b;
    }

    public static int min(int a, int b) {
        if (a <= b) return a;
        else return b;
    }

    BigNum add(BigNum a) {
        StringBuilder builder = new StringBuilder();
        int carrier = 0;

        for (int i = 0; i < max(length(), a.length()); i++) {

            byte aDigit = i < a.length() ? a.digits[i] : 0;
            byte thisDigit = i < length() ? digits[i] : 0;

            int result = aDigit + thisDigit + carrier;
            builder.append(result % 10);
            carrier = result / 10;
        }

        if (carrier > 0)
            builder.append(carrier);

        return BigNum.fromString(builder.reverse().toString());

    }

    BigNum subtract(BigNum a) {
        StringBuilder builder = new StringBuilder();
        byte[] thisdigits = digits.clone();


        if (a.isGreaterThan(this))
            throw new IllegalArgumentException("The parameter is bigger than this number");

        else {

            for (int i = 0; i < max(length(), a.length()); i++) {

                byte aDigit = i < a.length() ? a.digits[i] : 0;
                byte thisDigit = i < length() ? thisdigits[i] : 0;
                int result = thisDigit - aDigit;
                if (thisDigit - aDigit < 0) {
                    result = thisDigit - aDigit + 10;
                    thisdigits[i + 1]--;
                }


                builder.append(result);
            }

            return BigNum.fromString(builder.reverse().toString());

        }

    }


    BigNum divideBy(BigNum a) {
        int i, counter = 0;
        StringBuilder builder = new StringBuilder();
        StringBuilder result = new StringBuilder();
        BigNum M;

        if (a.compareTo(BigNum.ZERO) == 0) {
            throw new IllegalArgumentException("Cannot divide by zero!");
        }
        if (this.compareTo(a) == -1)
            return BigNum.ZERO;
        if (this.compareTo(a) == 0)
            return BigNum.ONE;


        do {
            int flag = 0;

            for (i = this.length() - 1 - counter; ((!BigNum.fromString(builder.toString()).isGreaterThan(a))) && (BigNum.fromString(builder.toString()).compareTo(a) == -1) && !(counter + 1 > this.length()); i--) {
                builder.append(digits[i]);
                counter++;

            }


            M = BigNum.fromString(builder.toString());

            for (i = 9; !(M.compareTo(a.multiply(i)) == 1) && !(M.compareTo(a.multiply(i)) == 0); i--) ;
            result.append(i);
            M = M.subtract(a.multiply(i));
            builder = new StringBuilder();
            builder.append(M.toString());


            for (i = this.length() - 1 - counter; ((!BigNum.fromString(builder.toString()).isGreaterThan(a))) && !(counter + 1 > this.length()); i--) {

                builder.append(digits[i]);
                counter++;
                flag++;
                System.out.println(counter + " " + this.length() + " " + digits[i] + " " + result + " " + builder + " " + i);
            }

            if (flag > 1) for (int j = 1; j < flag; j++) result.append("0");

            M = BigNum.fromString(builder.toString());

            if (!M.isGreaterThan(a) && flag != 0) result.append("0");


        } while (M.isGreaterThan(a));

        return BigNum.fromString(result.toString());
    }


    BigNum multiply(int A) {

        if (A == 0) return BigNum.ZERO;
        if (A == 1) return this;

        BigNum a = BigNum.fromLong(A);

        StringBuilder builder = new StringBuilder();
        int carrier = 0;
        long lengthk = min(length(), a.length());
        long lengthb = max(length(), a.length());

        BigNum n = new BigNum();
        if (a.length() >= length()) {
            for (int j = 0; j < lengthk; j++) {
                if (j != 0)
                    for (int i = 0; i < j; i++)
                        builder.append(0);

                for (int i = 0; i < lengthb; i++) {
                    byte aDigit = a.digits[i];
                    byte thisDigit = digits[j];
                    int result = aDigit * thisDigit + carrier;
                    builder.append(result % 10);
                    carrier = result / 10;
                }
                if (carrier > 0)
                    builder.append(carrier);
                n = n.add(BigNum.fromString(builder.reverse().toString()));
                builder.delete(0, builder.length());
                carrier = 0;
            }
        } else {

            for (int i = 0; i < lengthk; i++) {
                if (i != 0)
                    for (int j = 0; j < i; j++)
                        builder.append(0);

                for (int j = 0; j < lengthb; j++) {
                    byte aDigit = a.digits[i];
                    byte thisDigit = digits[j];
                    int result = aDigit * thisDigit + carrier;
                    builder.append(result % 10);
                    carrier = result / 10;
                }
                if (carrier > 0)
                    builder.append(carrier);
                n = n.add(BigNum.fromString(builder.reverse().toString()));
                builder.delete(0, builder.length());
                carrier = 0;

            }


        }
        return n;
    }


    BigNum multiply(BigNum a) {

        if (a.compareTo(BigNum.ZERO) == 0) return BigNum.ZERO;
        if (a.compareTo(BigNum.ONE) == 0) return this;

        StringBuilder builder = new StringBuilder();
        int carrier = 0;
        long lengthk = min(length(), a.length());
        long lengthb = max(length(), a.length());

        BigNum n = new BigNum();
        if (a.length() >= length()) {
            for (int j = 0; j < lengthk; j++) {
                if (j != 0)
                    for (int i = 0; i < j; i++)
                        builder.append(0);

                for (int i = 0; i < lengthb; i++) {
                    byte aDigit = a.digits[i];
                    byte thisDigit = digits[j];
                    int result = aDigit * thisDigit + carrier;
                    builder.append(result % 10);
                    carrier = result / 10;
                }
                if (carrier > 0)
                    builder.append(carrier);
                n = n.add(BigNum.fromString(builder.reverse().toString()));
                builder.delete(0, builder.length());
                carrier = 0;
            }
        } else {

            for (int i = 0; i < lengthk; i++) {
                if (i != 0)
                    for (int j = 0; j < i; j++)
                        builder.append(0);

                for (int j = 0; j < lengthb; j++) {
                    byte aDigit = a.digits[i];
                    byte thisDigit = digits[j];
                    int result = aDigit * thisDigit + carrier;
                    builder.append(result % 10);
                    carrier = result / 10;
                }
                if (carrier > 0)
                    builder.append(carrier);
                n = n.add(BigNum.fromString(builder.reverse().toString()));
                builder.delete(0, builder.length());
                carrier = 0;

            }


        }
        return n;
    }

    public static void main(String[] args) {


        // Usage  :)
        BigNum a = new BigNum();
        BigNum b = new BigNum();

        a = BigNum.fromString("5000");
        b = BigNum.fromString("2000");
        System.out.println(a.subtract(b));
        System.out.println(a.multiply(b));
        System.out.println(a.divideBy(b));
        System.out.println(a.add(b));
        System.out.println(b.divideBy(a));
    }


}




