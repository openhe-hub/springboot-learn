# SpringBoot Note 5：Springboot + Junit5 单元测试
1. Junit 5 常用测试注解
    * @Test :表示方法是测试方法。但是与JUnit4的@Test不同，他的职责非常单一不能声明任何属性，拓展的测试将会由Jupiter提供额外测试
    * @ParameterizedTest :表示方法是参数化测试，下方会有详细介绍
    * @RepeatedTest :表示方法可重复执行，下方会有详细介绍
    * @DisplayName :为测试类或者测试方法设置展示名称
    * @BeforeEach :表示在每个单元测试之前执行
    * @AfterEach :表示在每个单元测试之后执行
    * @BeforeAll :表示在所有单元测试之前执行
    * @AfterAll :表示在所有单元测试之后执行
    * @Tag :表示单元测试类别，类似于JUnit4中的@Categories
    * @Disabled :表示测试类或测试方法不执行，类似于JUnit4中的@Ignore
    * @Timeout :表示测试方法运行如果超过了指定时间将会返回错误
    * @ExtendWith :为测试类或测试方法提供扩展类引用   
    ```java
    @SpringBootTest
    class DemoJunitApplicationTests {

        @Test
        @DisplayName("annotation for test")
        void testDisplayName(){
            System.out.println("Hello Junit 5");
        }

        @Test
        void test2(){
            System.out.println("Hello Junit 5");
        }

        @Test
        @Timeout(value = 500,unit = TimeUnit.MILLISECONDS)
        void testTimeOut() throws InterruptedException {
            Thread.sleep(1000);
        }

        @Test
        @RepeatedTest(value = 10)
        void testRepeat(){
            System.out.println("repeating");
        }

        @BeforeEach
        void testBefore(){
            System.out.println("before test");
        }

        @AfterEach
        void testAfter(){
            System.out.println("after test");
        }

        @BeforeAll
        static void testBeforeAll(){
            System.out.println("before all tests");
        }

        @AfterAll
        static void testAfterAll(){
            System.out.println("after all tests");
        }
    }
    ``` 
2. Junit断言机制Assertion
    * assertEquals	判断两个对象或两个原始类型是否相等
    * assertNotEquals	判断两个对象或两个原始类型是否不相等
    * assertSame	判断两个对象引用是否指向同一个对象
    * assertNotSame	判断两个对象引用是否指向不同的对象
    * assertTrue	判断给定的布尔值是否为 true
    * assertFalse	判断给定的布尔值是否为 false
    * assertNull	判断给定的对象引用是否为 null
    * assertNotNull	判断给定的对象引用是否不为 null
   ```java
    @SpringBootTest
    public class AssertTest {
        @Test
        void testSimpleAssertions() {
            int ret = calc(1, 1);
    //        Assertions.assertEquals(2, ret);
            Assertions.assertEquals(3, ret, "calc function error!");
        }

        @Test
        @DisplayName("array assertion")
        public void testArray() {
            Assertions.assertArrayEquals(new int[]{1, 2}, new int[]{2, 1});
        }

        @Test
        public void testAll() {
            Assertions.assertAll("test all",
                    () -> Assertions.assertTrue(true),
                    () -> Assertions.assertEquals(1, 2));
        }

        @Test
        public void testException() {
            Assertions.assertThrows(ArithmeticException.class,
                    () -> {
                        int i = 10 / 0;
                    },"divided by zero");
        }

        @Test
        public void timeoutTest() {
            //如果测试方法时间超过1s将会异常
            Assertions.assertTimeout(Duration.ofMillis(1000), () -> Thread.sleep(500));
        }

        @Test
        @DisplayName("fail")
        public void shouldFail() {
            Assertions.fail("This should fail");
        }

        int calc(int i, int j) {
            return i + j;
        }
    }
   ``` 
3. 前置条件Assumption：不符合前置条件，测试终止
   ```java
    @Test
    void testAssumptions(){
        Assumptions.assumeTrue(false);
        System.out.println("after assumption");
    }
   ``` 
4. 嵌套测试：官网案例
   ```java
    @DisplayName("A stack")
    class TestingAStackDemo {

        Stack<Object> stack;

        @Test
        @DisplayName("is instantiated with new Stack()")
        void isInstantiatedWithNew() {
            new Stack<>();
        }

        @Nested
        @DisplayName("when new")
        class WhenNew {

            @BeforeEach
            void createNewStack() {
                stack = new Stack<>();
            }

            @Test
            @DisplayName("is empty")
            void isEmpty() {
                assertTrue(stack.isEmpty());
            }

            @Test
            @DisplayName("throws EmptyStackException when popped")
            void throwsExceptionWhenPopped() {
                assertThrows(EmptyStackException.class, stack::pop);
            }

            @Test
            @DisplayName("throws EmptyStackException when peeked")
            void throwsExceptionWhenPeeked() {
                assertThrows(EmptyStackException.class, stack::peek);
            }

            @Nested
            @DisplayName("after pushing an element")
            class AfterPushing {

                String anElement = "an element";

                @BeforeEach
                void pushAnElement() {
                    stack.push(anElement);
                }

                @Test
                @DisplayName("it is no longer empty")
                void isNotEmpty() {
                    assertFalse(stack.isEmpty());
                }

                @Test
                @DisplayName("returns the element when popped and is empty")
                void returnElementWhenPopped() {
                    assertEquals(anElement, stack.pop());
                    assertTrue(stack.isEmpty());
                }

                @Test
                @DisplayName("returns the element when peeked but remains not empty")
                void returnElementWhenPeeked() {
                    assertEquals(anElement, stack.peek());
                    assertFalse(stack.isEmpty());
                }
            }
        }
    }
   ``` 
5. 参数测试
    * @ValueSource: 为参数化测试指定入参来源，支持八大基础类以及String类型,Class类型
    * @NullSource: 表示为参数化测试提供一个null的入参
    * @EnumSource: 表示为参数化测试提供一个枚举入参
    * @CsvFileSource：表示读取指定CSV文件内容作为参数化测试入参
    * @MethodSource：表示读取指定方法的返回值作为参数化测试入参(注意方法返回需要是一个流)
   1. 注解直接注入参数
      ```java
        @ParameterizedTest
        @ValueSource(ints = {1, 2, 3, 4, 5})
        void testParam(int i) {
            System.out.println(i);
        }
      ``` 
   2. 方法注入参数
      ```java
        @ParameterizedTest
        @MethodSource("generateData")
        void testParamFromMethod(int i) {
            System.out.println(i);
        }

        static int[] generateData() {
            return new int[]{1, 2, 3};
        }
      ``` 