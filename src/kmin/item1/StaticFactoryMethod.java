package kmin.item1;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.FileStore;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

/**
 * 객체를 얻기 위해 public 생성자 대신 정적 팩터리 메서드를 제공할 수 있다.
 */
public class StaticFactoryMethod {
    public static void main(String[] args) throws IOException {
        // 장점1. 이름을 가질 수 있다.

        // 똑같이 특정 조건의 소수를 얻는 코드지만 생성자보다는
        // 정적 팩터리 메서드가 의미를 분명히 할 수 있다.
        BigInteger bi1 = new BigInteger(8, 1, new Random());
        System.out.println(bi1);
        BigInteger bi2 = BigInteger.probablePrime(8, new Random());
        System.out.println(bi2);

        /*
        또한 하나의 시그니쳐로는 하나의 생성자만 만들 수 있다.
        시그니쳐는 같지만 다른 역할을 하는 생성자가 필요할 경우
        파라미터 위치 등을 바꾸는 꼼수도 있겠지만 당연히 가독성이 떨어진다.
        이런 점에서 정적 팩토리 메서드는 의미를 분명하게 할 수 있다.
         */

        // 장점2. 호출될 때마다 인스턴스를 새로 생성하지는 않아도 된다.

        // 아래는 이미 만들어진 동일한 객체를 리턴한다.
        Boolean a = Boolean.valueOf("true");

        /*
        이와 같이 정적 팩터리 방식의 클래스는 인스턴스 생성의 통제가 용이함
        이런 클래스를 인스턴스 통제 클래스라고도 부름
        인스턴스 통제는 플라이웨이트 패턴의 근간임 됨.

        인스턴스 통제의 활용 예제
        - 싱글턴 패턴
        - 인스턴스화 불가 클래스
        - 불변 값 클래스에서 동치인 인스턴스가 단 하나뿐임을 보장할 수 있음
          - a == b 일 때만 a.equals(b)가 성립
         */

        // 장점3. 반환 타입의 하위 타입 객체를 반환할 수 있음
        /*
        반활할 객체의 클래스를 자유롭게 선택할 수 있는 유연성을 제공함
        구현 클래스를 공개하지 않고 그 객체를 반환할 수 있으므로 API를 작게 유지 가능함
        인터페이스를 정적 팩터리 메서드의 반환 타입으로 사용하는 인터페이스 기반 프레임워크의 핵심 기술이기도 함

        자바 8 전에는 인터페이스에 정적 메서드 선언이 불가했으므로
        이름이 "Type"인 인터페이스를 반환하는 정적 메서드가 필요하면
        "Types" 라는 인스턴스화 불가한 동반 클래스를 함께 제공하는 관례가 있었음

        대표적으로 java.util.Collection 과 java.util.Collections
        자바 컬렉션 프레임워크에는 수정 불가나 동기화 기능을 덧붙힌
        45개의 유틸리티 구현체를 제공하는데 대부분을 Collections 의 정적 팩터리 메서드로 얻을 수 있음
        이를 통해 개발자가 익혀야할 API를 최소화하고 개발자는 인터페이스만으로 해당 객체를 다루게됨

        자바 8 부터는 인터페이스도 정적 메서드를 가질 수 있으므로
        동반 클래스에 두었던 public 정적 멤버들을 인터페이스 자체에 둘 수도 있음
        단, 자바 8은 인터페이스에 public 정적 멤버만 허용하고
        자바9는 private 정적 메서드까지느 허용하지만
        정적 필드, 정적 멤버 클래스는 여전히 public이어야하는 등 일부 제약이 존재함.
        따라서 정적 메서드들을 구현하기 위한 코드 중 많은 부분은 여전히
        package-private 클래스에 두어야할 수 있음
         */

        // 장점4. 입력 매개변수에 따라 매번 다른 클래스의 객체를 반환할 수 있음
        /*
        반환 타입의 하위 타입이기만 하면 어떤 클래스의 객체를 반환해도 됨
        다음 릴리즈에서 다른 객체를 반환하는 것도 가능
         */

        /*
        EnumSet을 예로 들면 allOf 메서드의 경우
        원소가 64개 이하면 RegularEnumSet
        초과하면 JumboEnumSet 
        객체를 반환한다.
        
        사용자는 이런 세부사항을 몰라도 된다.
        다음 릴리스에서는 필요없는 구현체를 제거하거나
        더 성능이 좋은 구현체로 변경하는 것도 가능하다.
         */
        EnumSet<EnumTest> smallEnumSet = EnumSet.allOf(EnumTest.class);

        // package-private 클래스이므로 여기서는 직접 생성이 불가능하다
        // new RegularEnumSet(...) // compile error!

        // 장점5. 정적 팩토리 메서드를 작성하는 시점에는 반환할 객체의 클래스가 존재하지 않아도 된다.
        /*
        이 유연함이 서비스 제공자 프레임워크를 만드는 근간
        대표적인 Service Provider Framework는 JDBC

        서비스 제공자 프레임워크의 3개 핵심 컴포넌트
        1. Service Interface : 구현체의 동작을 정의
        2. Provider Registration API : 제공자가 구현체를 등록할 때 사용
        3. Service Access API : 클라이언트가 서비스의 인스턴스를 얻을 때 사용
        - 사용자는 원하는 구현체의 조건을 명시할 수 있음

        추가로 쓰이는 네 번째 컴포넌트
        4. Service Provider Interface : 인스턴스를 생성하는 팩터리 객체를 설명해줌
        - 이것이 없으면 각 구현체를 인스턴스토 만들 때 리플렉션을 사용해야함

        ex) JDBC
        1. Connection -> Service Interface
        2. DriverManager.registerDriver -> Provider Registration API
        3. DriverManager.getConnection -> Service Access API
        4. Driver -> Service Provider Interface

        서비스 제공자 프레임워크 패턴의 변형
        1. 브리지 패턴 : 서비스 접근 API가 공급자가 제공하는 것보다 풍부한 서비스 인터페이스를 클라이언트에 반환
        2. 의존 객체 주입 프레임워크 : 서비스 제공자의 일종
        3. java5부터는 java.util.ServiceLoader 라는 범용 서비스 제공자 프레임워크가 제공됨
        */

        // 단점1. 상속을 하려면 public이나 protected 생성자가 필요하니 정적 팩터리 메서드만
        // 제공하면 하위 클래스를 만들 수 없음
        /*
        예를 들면 Collections 는 상속할 수 없다.
        그러나 상속보다는 컴포지션을 사용하도록 유도하고
        불변 타입으로 만들려면 이 제약을 지켜야한다는 점에서 장점이기도 함
         */

        // 단점2. 정적 팩터리 메서드는 프로그래머가 찾기 어렵다
        /*
        생성자는 javadoc 등의 API에서 바로 구분할 수 있지만
        정적 팩토리 메서드는 문서를 꼼꼼히 읽어볼 수 밖에 없음.
        대안은 메서드 네이밍을 알려진 규약에 최대한 맞추는 것.
         */

        // from : 매개변수를 하나 받아 해당 타입의 인스턴스를 반환하는 형변환 메서드
        Date d = Date.from(Instant.from(LocalDate.now()));

        // of : 여러 매개변수를 받아 적합한 타입의 인스턴스를 반환하는 집계 메서드
        EnumSet<EnumTest> set1 = EnumSet.of(EnumTest.A, EnumTest.B);

        // valueOf : from, of 의 자세한 버전
        BigInteger bigNumber = BigInteger.valueOf(Long.MAX_VALUE);

        // instance 혹은 getInstance : 조건에 맞는 인스턴스를 반환하지만 같은 인스턴스임은 보장하지 않음

        // create 혹은 newInstance : getInstance와 유사하지만 매번 새로운 인스턴스를 생성해 반환함을 보장

        // getType : getInstance와 유사하나 생성할 클래스가 아닌 다른 클래스에 팩터리 메서드를 정의할 때 싸용
        FileStore fs = Files.getFileStore(Path.of("filepath..."));

        // newType : newInstance와 유사하나 생성할 클래스가 아닌 다른 클래스에 팩터리 메서드를 정의할 때 싸용
        BufferedReader br = Files.newBufferedReader(Path.of("filepath..."));

        // type : getType, newType 의 간결할 버전
        // List<Data> dataList = Collections.list(targets);

        /*
        결론

        정적 팩터리 메서드와 public 생성자는 각각 쓰임새가 있음.
        일반적으로 정적 팩터리 메서드가 유리한 경우가 많으므로
        무작정 public 생성자를 제공하기 전에 정적 팩터리 메서드를 검토할 것.
         */
    }
        private enum EnumTest {
        A, B, C
    }
}
