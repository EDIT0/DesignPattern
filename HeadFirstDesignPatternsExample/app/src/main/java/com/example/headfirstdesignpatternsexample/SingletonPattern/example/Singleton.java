package com.example.headfirstdesignpatternsexample.SingletonPattern.example;

import com.example.headfirstdesignpatternsexample.Utility;

public class Singleton {

    private String TAG = Singleton.class.getSimpleName();

    private static Singleton uniqueInstance;

    private Singleton() {

    }

    public static Singleton getInstance() {
        if(uniqueInstance == null) {
            /**
             * 멀티스레드일 경우 uniqueInstance가 null일 때 함께 들어와 객체를 2개 만들 가능성이 있다.
             * 해결방법
             * 1. synchronized를 사용한다.
             * 단점: 성능 저하
             * 2. 처음부터 인스턴스 생성
             * private static Singleton uniqueInstance = new Singleton();
             * public static Singleton getInstance() {
             *      return uniqueInstance;
             * }
             * 단점: 사용되지 않을 때도 이미 객체가 생성되어 있다.
             * 3. DCL(Double-Checked Locking)
             * private volatile static Singleton uniqueInstance;
             * public static Singleton getInstance() {
             *      if(uniqueInstance == null) { // 인스턴스가 있는 확인, 없으면 동기화된 블록으로 들어간다.
             *          synchronized(Singleton.class) { // 이러면 처음에만 동기화된다.
             *              if(uniqueInstance == null) {
             *                  uniqueInstance = new Singleton();
             *             }
             *          }
             *      }
             *      return uniqueInstance;
             * }
             * 단점: volatile은 자바 5 이전에 나온 버전에서 사용 불가
             *
             * 위에 나온 방법들은 리플렉션, 역직렬화, 직렬화에도 문제가 된다.
             * 그래서 enum을 사용하면 된다.
             *
             * 다른 접근. 전역변수를 사용하면 될거 같은데..
             * 1. 전역변수는 lazy하게 사용할 수 없다.
             * 2. 클래스가 하나의 인스턴스만 가지도록 할 수 없다.
             * */
            try {
                Thread.sleep(100L);
            } catch (Exception e) {

            }
            Utility.INSTANCE.Log("Singleton", "객체 만들기");
            uniqueInstance = new Singleton();
        }
        return uniqueInstance;
    }
}
