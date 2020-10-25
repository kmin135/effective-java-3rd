package kmin.item1;

import java.math.BigInteger;
import java.util.Random;

/**
 * 객체를 얻기 위해 public 생성자 대신 정적 팩터리 메서드를 제공할 수 있다.
 */
public class StaticFactoryMethod {
    public static void main(String[] args) {
        // 장점1. 이름을 가질 수 있다.

        // 똑같이 특정 조건의 소수를 얻는 코드지만 생성자보다는 정적 팩터리 메서드가 의미를 분명히 할 수 있다.
        BigInteger bi1 = new BigInteger(8, 1, new Random());
        System.out.println(bi1);
        BigInteger bi2 = BigInteger.probablePrime(8, new Random());
        System.out.println(bi2);
    }
}
