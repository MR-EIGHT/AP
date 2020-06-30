package com.eight.num;


import java.util.Arrays;
import java.util.Random;

//import java.math.BigInteger
public class BigNum {

    public static final BigNum ZERO = new BigNum(0);
    public static final BigNum ONE = new BigNum(1);

    private static final Byte[] evenNumbers = {0, 2, 4, 6, 8};
    private static final Byte[] oddNumbers = {1, 3, 5, 7, 9};
    private final byte[] digits;
    private boolean signed;

    private String clean(String s) {
        s = s.trim();

        if (s.length() != 0 && s.charAt(0) == '-') signed = true;
        else signed = false;

        int counter = 0;
        for (int i = 0; i < s.length(); i++)
            if (s.charAt(i) == '0')
                counter++;
        if (counter == s.length()) return "0";

        StringBuilder digits = new StringBuilder(s.length());
        boolean leadingZero = true;
        for (int i = 0; i < s.length(); i++) {
            if (i == 0 && (s.charAt(i) == '-' || s.charAt(i) == '+'))
                continue;
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
        builder = builder.reverse();

        if (this.signed && !builder.toString().equals("0")) builder.insert(0, '-');
        return builder.toString();

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

    public int compareTo(BigNum a) {

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

    private BigNum adder(BigNum a) {

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

    private BigNum subtracter(BigNum a) {
        StringBuilder builder = new StringBuilder();
        byte[] ThisDigits = digits.clone();


        //  if (a.isGreaterThan(this))

        //throw new IllegalArgumentException("The parameter is bigger than this number");

        // else {

        for (int i = 0; i < max(length(), a.length()); i++) {

            byte aDigit = i < a.length() ? a.digits[i] : 0;
            byte thisDigit = i < length() ? ThisDigits[i] : 0;
            int result = thisDigit - aDigit;
            if (thisDigit - aDigit < 0) {
                result = thisDigit - aDigit + 10;
                ThisDigits[i + 1]--;
            }


            builder.append(result);
        }

        return BigNum.fromString(builder.reverse().toString());

    }

    // }


    public BigNum divideBy(BigNum a) {
        int i, counter = 0;
        StringBuilder builder = new StringBuilder();
        StringBuilder result = new StringBuilder();
        BigNum M;

        if (this.compareTo(a) == 0) return BigNum.ONE;
        if (a.compareTo(BigNum.ZERO) == 0) {
            throw new ArithmeticException("Cannot divide by zero!");
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

            for (i = 9; !(M.compareTo(a.multiply(i)) == 1) && M.compareTo(a.multiply(i)) == -1; i--) ;
            result.append(i);
            M = M.subtracter(a.multiply(i));
            builder = new StringBuilder();
            builder.append(M.toString());


            for (i = this.length() - 1 - counter; ((!BigNum.fromString(builder.toString()).isGreaterThan(a))) && !(counter + 1 > this.length()); i--) {

                builder.append(digits[i]);
                counter++;
                flag++;
                if (BigNum.fromString(builder.toString()).compareTo(a) == 0) break;

            }
            int flag2 = 1;
            if (flag > 1) for (int j = 1; j < flag; j++) result.append("0");

            M = BigNum.fromString(builder.toString());


            if ((BigNum.fromString(result.toString()).multiply(a).equals(this))) break;
            if (this.subtract(BigNum.fromString(result.toString()).multiply(a)).compareTo(a) == -1) break;
            if (builder.length() > 1 && builder.charAt(0) == '0' && BigNum.fromString(builder.toString()).equals(BigNum.ZERO)) {
                builder.deleteCharAt(0);
                for (int x = 1; x < flag; x++)
                    builder.deleteCharAt(0);
                result.append(builder.toString());
            } else if (counter == this.length() && BigNum.fromString(builder.toString()).compareTo(a) == -1)
                result.append("0");
//            if (!M.isGreaterThan(a) && flag != 1&& BigNum.fromLong(digits[counter - 1]).compareTo(M) != 0 &&BigNum.fromLong(digits[counter - 1]).compareTo(M) != -1 && counter != this.length())
//                result.append("0");
//           else if (!M.isGreaterThan(a) && flag != 0 &&flag2!=0&& BigNum.fromLong(digits[counter - 1]).compareTo(M) != 1 && counter == this.length() ||(( BigNum.fromString(builder.toString()).compareTo(a)==-1)&& counter==this.length() ))
//                result.append("0");
        } while (M.isGreaterThan(a) || counter != this.length() || M.compareTo(a) != -1);


        BigNum n = BigNum.fromString(result.toString());
        if (this.signed ^ a.signed) n.signed = true;
        return n;
    }


    public BigNum multiply(int A) {

        if (A == 0) return BigNum.ZERO;
        if (A == 1) return this;

        BigNum a = BigNum.fromLong(A);

        StringBuilder builder = new StringBuilder();
        int carrier = 0;
        long lengthK = min(length(), a.length());
        long lengthB = max(length(), a.length());

        BigNum n = new BigNum();
        if (a.length() >= length()) {
            for (int j = 0; j < lengthK; j++) {
                if (j != 0)
                    for (int i = 0; i < j; i++)
                        builder.append(0);

                for (int i = 0; i < lengthB; i++) {
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

            for (int i = 0; i < lengthK; i++) {
                if (i != 0)
                    for (int j = 0; j < i; j++)
                        builder.append(0);

                for (int j = 0; j < lengthB; j++) {
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
        if (this.signed ^ a.signed) n.signed = true;


        return n;

    }


    public BigNum multiply(BigNum a) {

        if (a.compareTo(BigNum.ZERO) == 0) return BigNum.ZERO;
        if (a.compareTo(BigNum.ONE) == 0) return this;

        StringBuilder builder = new StringBuilder();
        StringBuilder finalResult = new StringBuilder();
        int carrier = 0;
        long lengthK = min(length(), a.length());
        long lengthB = max(length(), a.length());

        BigNum n = new BigNum();
        if (a.length() >= length()) {

            for (int j = 0; j < lengthK; j++) {
                if (j != 0)
                    for (int i = 0; i < j; i++)
                        builder.append(0);

                for (int i = 0; i < lengthB; i++) {
                    byte aDigit = a.digits[i];
                    byte thisDigit = digits[j];
                    int result = aDigit * thisDigit + carrier;
                    builder.append(result % 10);
                    carrier = result / 10;
                }
                if (carrier > 0)
                    builder.append(carrier);

                n = n.add(BigNum.fromString(builder.reverse().toString()));
                builder = new StringBuilder();
                carrier = 0;
            }
        } else {

            for (int i = 0; i < lengthK; i++) {
                if (i != 0)
                    for (int j = 0; j < i; j++)
                        builder.append(0);

                for (int j = 0; j < lengthB; j++) {
                    byte aDigit = a.digits[i];
                    byte thisDigit = digits[j];
                    int result = aDigit * thisDigit + carrier;
                    builder.append(result % 10);
                    carrier = result / 10;
                }
                if (carrier > 0)
                    builder.append(carrier);
                n = n.add(BigNum.fromString(builder.reverse().toString()));
                builder = new StringBuilder();
                carrier = 0;

            }


        }
        if (this.signed ^ a.signed) n.signed = true;


        return n;
    }


    public BigNum add(BigNum a) {
        if (!a.signed && !this.signed) return this.adder(a);

        if (a.signed && this.signed) {
            BigNum c = this.adder(a);
            c.signed = true;
            return c;
        }

        if (this.signed) {
            BigNum c;
            if (this.compareTo(a) == 1)
                c = this.subtracter(a);
            else c = a.subtracter(this);

            if (this.compareTo(a) == 1) c.signed = true;
            else c.signed = false;
            return c;
        }


        BigNum c;
        if (this.compareTo(a) == 1)
            c = this.subtracter(a);
        else c = a.subtracter(this);

        if (this.compareTo(a) == 1) c.signed = false;
        else c.signed = true;
        return c;


    }


    public BigNum subtract(BigNum a) {
        BigNum c = new BigNum();

        if (!this.signed && !a.signed) {

            if (this.compareTo(a) == 1)
                c = this.subtracter(a);
            if (this.compareTo(a) == -1) {
                c = a.subtracter(this);
                c.signed = true;
            }
        }

        if (this.signed && a.signed) {
            if (this.compareTo(a) == 1) {
                c = this.subtracter(a);
                c.signed = true;
            }
            if (this.compareTo(a) == -1) {
                c = a.subtracter(this);
                c.signed = false;
            }
        }

        if (!this.signed && a.signed) {
            c = this.adder(a);
            c.signed = false;

        }

        if (this.signed && !a.signed) {
            c = this.adder(a);
            c.signed = true;

        }


        return c;
    }


    public BigNum mod(BigNum a) {

        BigNum quotient = this.divideBy(a);
        return this.subtract(quotient.multiply_Positive_fast(a));

    }





    public BigNum Pow(BigNum a) {
        if(a.signed) throw new IllegalArgumentException("Bad input");

        BigNum res = this;
        for (BigNum i = BigNum.ONE; i.compareTo(a) != 0; i = i.add(BigNum.ONE)) {
            if (i.compareTo(BigNum.ONE) != 0 && a.mod(i).compareTo(BigNum.ZERO) == 0 && a.divideBy(i).compareTo(BigNum.fromLong(2)) == 0) {
                res = res.multiply(res);
                break;
            } else res = res.multiply(this);

        }

        return res;
    }


    public static BigNum Random(int length, boolean odd) {
        int sum = 0;
        StringBuilder randnum = new StringBuilder();
        int randD = (int) (Math.random() * ((9) + 1));
        for (int i = 0; i < length; i++) {
            randD = ((int) (Math.random() * ((9) + 1)));
            BigNum.fromLong(randD);
            if (i == 0)
                while (randD == 0) randD = ((int) (Math.random() * ((9) + 1)));
            if (i == length - 1)
                while (!BigNum.fromLong(randD).isOdd() || BigNum.fromLong(randD).compareTo(BigNum.fromLong(5)) == 0)
                    randD = ((int) (Math.random() * ((9) + 1)));
            sum = sum + randD;
            randnum.append(randD);
        }
        if (sum % 3 == 0) Random(length, true);
       // if ((BigNum.fromString(randnum.toString()).subtract(BigNum.fromLong(2 * (int) randnum.charAt(randnum.length() - 1))).mod(BigNum.fromLong(7)).equals(BigNum.ZERO)))
         //   Random(length, true);
        return BigNum.fromString(randnum.toString());

    }

    public StringBuilder binarize() {
        StringBuilder bits = new StringBuilder();
        BigNum localthis = this;
        for (BigNum i = localthis; i.compareTo(BigNum.ZERO) == 1; i = i.divideBy(BigNum.fromLong(2))) {
            bits.append(i.mod(BigNum.fromLong(2)).toString());

        }
        bits = bits.reverse();
        return bits;

    }

    public static BigNum Decimalize(String n) {
        String num = n;
        BigNum dec_value = BigNum.ZERO;


        BigNum base = BigNum.ONE;

        int len = num.length();
        for (int i = len - 1; i >= 0; i--) {
            if (num.charAt(i) == '1')
                dec_value = dec_value.add(base);
            base = base.multiply(2);
        }

        return dec_value;
    }


    public static String shift(StringBuilder bits, int k) {
        char temp;
        int n = bits.length();
        if (k > 0) {

            for (int j = 0; j < k; j++) {
                temp = bits.charAt(n - 1);
                for (int i = n - 1; i > 0; i--)
                    bits.setCharAt(i, bits.charAt(i - 1));
                bits.setCharAt(0, temp);

            }
        }
        if (k < 0) {
            k = -k;

            for (int j = 0; j < k; j++) {
                temp = bits.charAt(0);
                for (int i = 0; i < n - 1; i++)
                    bits.setCharAt(i, bits.charAt(i + 1));
                bits.setCharAt(n - 1, temp);

            }
        }

        return bits.toString();
    }

    public Boolean isOdd() {
        for (int odd : oddNumbers) {
            if (this.digits[0] == odd)
                return true;
        }
        return false;
    }


    private static BigNum power(BigNum x, BigNum y, BigNum p) {

        BigNum res = BigNum.ONE;


        x = x.mod(p);

        while (y.compareTo(BigNum.ZERO) == 1) {

            if (y.isOdd())
                res = (res.multiply_Positive_fast(x)).mod(p);

            y = y.divideBy(BigNum.fromLong(2));
            x = x.multiply_Positive_fast(x).mod(p);
        }

        return res;
    }

   private  static boolean miillerTest(BigNum d, BigNum n) {
        Random ran = new Random();

        BigNum a = BigNum.fromLong(Math.abs(ran.nextLong()));



        // Compute a^d % n
        BigNum x = power(a, d, n);

        if (x.equals(BigNum.ONE) || x.equals(n.subtract(BigNum.ONE)))
            return true;


        while (!d.equals(n.subtract(BigNum.ONE))) {
            x = x.multiply_Positive_fast(x).mod(n);

            d = d.multiply(2);

            if (x.equals(BigNum.ONE))
                return false;
            if (x.equals(n.subtract(BigNum.ONE)))
                return true;
        }

        return false;
    }


    public static boolean isPrime(BigNum n, int k) {

        for (Byte even : evenNumbers) {
            if (n.digits[0] == even)
                return false;
        }



        if (!n.isGreaterThan(BigNum.ONE) || n.equals(BigNum.fromLong(4)))
            return false;
        if (!n.isGreaterThan(BigNum.fromLong(3)))
            return true;


        BigNum d = n.subtract(BigNum.ONE);

        while (d.mod(BigNum.fromLong(2)).equals(BigNum.ZERO))
            d = d.divideBy(BigNum.fromLong(2));


        for (int i = 0; i < k; i++)
            if (!miillerTest(d, n))
                return false;

        return true;
    }


    public static BigNum generatePrime(int length) {


        BigNum prime = Random(length, true);
        while (!isPrime(prime, 10))
            prime = Random(length, true);

        return prime;

    }


    private static BigNum multiplyKaratsuba(BigNum x, BigNum y) {
        int size1 = x.length();
        int size2 = y.length();

        int N = Math.max(size1, size2);

        if (N < 5)
            return x.multiply(y);


        N = (N / 2) + (N % 2);

        BigNum m = po(N);


        BigNum b = x.divideBy(m);
        BigNum a = x.subtract(b.multiply(m));
        BigNum d = y.divideBy(m);
        BigNum c = y.subtract(d.multiply(m));

        BigNum z0 = multiplyKaratsuba(a, c);
        BigNum z2 = multiplyKaratsuba(b, d);
        BigNum z1 = multiplyKaratsuba(a.add(b), c.add(d));


        return z0.add((z1.subtract(z0).subtract(z2)).multiply(po(N))).add(z2.multiply(po(N * 2)));
        //    return z0 + (z1 - z0 - z2 << m) + (z2 << 2 * m);
    }


    private static BigNum po(int N) {
        BigNum m = BigNum.ONE;

        for (int i = 0; i < N; i++) {
            m = m.multiply(2);
        }
        return m;

    }


    private BigNum multiply_Positive_fast(BigNum a) {
        StringBuilder first = new StringBuilder();
        StringBuilder second = new StringBuilder();

        first.append(this.toString());
        second.append(a.toString());


        int length1 = first.length();
        int length2 = second.length();

        int[] answer = new int[length1 + length2];

        int Oin = 0;
        int Tin = 0;

        for (int digit1 = length1 - 1; digit1 >= 0; digit1--) {
            int carry = 0;
            int currentDigitOfOne = first.charAt(digit1) - '0';

            Tin = 0;

            for (int digit2 = length2 - 1; digit2 >= 0; digit2--) {
                int currentDigitOfTwo = second.charAt(digit2) - '0';

                int sum = (currentDigitOfOne * currentDigitOfTwo) + answer[Oin + Tin] + carry;

                carry = sum / 10;

                answer[Oin + Tin] = sum % 10;

                Tin++;
            }
            if (carry > 0) {
                answer[Oin + Tin] += carry;
            }

            Oin++;
        }


        StringBuilder n = new StringBuilder();
        int i = 0;
        while (i <= answer.length - 1) {
            n.append(answer[i]);
            i++;
        }


        BigNum resultNum = BigNum.fromString(n.reverse().toString());
        resultNum.signed = false;
        return resultNum;
    }










    public static void main(String[] args) {


        // Usage  :)
        BigNum a = new BigNum();
        a = BigNum.fromString("36");


       // System.out.println(generatePrime(100));

    }


}





