# XML解析方法

本项目通过解析天气预报的文件来分析XML文件的两种解析方式：

- XML数据的Dom解析
- XML数据的Pull解析
- XML数据的Sax解析

## 1.获取XML文件

本项目直接在Asset文件夹中模拟创建XML数据：

```xml
<students>
    <student>
        <name sex="man">小明</name>
        <nickName>明明</nickName>
    </student>
    <student>
        <name sex="woman">小红</name>
        <nickName>红红</nickName>
    </student>
    <student>
        <name sex="man">小亮</name>
        <nickName>亮亮</nickName>
    </student>
</students>
```

## 2.创建对应XML的Bean对象

```java
public class Student {
    private String name;
    private String sex;
    private String nickName;

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                ", nickName='" + nickName + '\'' +
                '}';
    }
}
```


## 3.Dom解析

- DOM解析XML文件时，会将XML文件的所有内容读取到内存中（内存的消耗比较大），然后允许您使用DOM API遍历XML树、检索所需的数据。

```java
public List<Student> dom2xml(InputStream is) throws Exception {
        //一系列的初始化
        List<Student> list = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        //获得Document对象
        Document document = builder.parse(is);
        //获得student的List
        NodeList studentList = document.getElementsByTagName("student");
        //遍历student标签
        for (int i = 0; i < studentList.getLength(); i++) {
            //获得student标签
            Node node_student = studentList.item(i);
            //获得student标签里面的标签
            NodeList childNodes = node_student.getChildNodes();
            //新建student对象
            Student student = new Student();
            //遍历student标签里面的标签
            for (int j = 0; j < childNodes.getLength(); j++) {
                //获得name和nickName标签
                Node childNode = childNodes.item(j);
                //判断是name还是nickName
                if ("name".equals(childNode.getNodeName())) {
                    String name = childNode.getTextContent();
                    student.setName(name);
                    //获取name的属性
                    NamedNodeMap nnm = childNode.getAttributes();
                    //获取sex属性，由于只有一个属性，所以取0
                    Node n = nnm.item(0);
                    student.setSex(n.getTextContent());
                } else if ("nickName".equals(childNode.getNodeName())) {
                    String nickName = childNode.getTextContent();
                    student.setNickName(nickName);
                }
            }
            //加到List中
            list.add(student);
        }
        return list;
    }
```


## 4.Pull解析

- Pull解析器的运行方式与 SAX 解析器相似。

```java
public List<Student> pull2xml(InputStream is) throws Exception {
        List<Student> list = null;
        Student student = null;
        //创建xmlPull解析器
        XmlPullParser parser = Xml.newPullParser();
        ///初始化xmlPull解析器
        parser.setInput(is, "utf-8");
        //读取文件的类型
        int type = parser.getEventType();
        //无限判断文件类型进行读取
        while (type != XmlPullParser.END_DOCUMENT) {
            switch (type) {
                //开始标签
                case XmlPullParser.START_TAG:
                    if ("students".equals(parser.getName())) {
                        list = new ArrayList<>();
                    } else if ("student".equals(parser.getName())) {
                        student = new Student();
                    } else if ("name".equals(parser.getName())) {
                        //获取sex属性
                        String sex = parser.getAttributeValue(null,"sex");
                        student.setSex(sex);
                        //获取name值
                        String name = parser.nextText();
                        student.setName(name);
                    } else if ("nickName".equals(parser.getName())) {
                        //获取nickName值
                        String nickName = parser.nextText();
                        student.setNickName(nickName);
                    }
                    break;
                //结束标签
                case XmlPullParser.END_TAG:
                    if ("student".equals(parser.getName())) {
                        list.add(student);
                    }
                    break;
            }
            //继续往下读取标签类型
            type = parser.next();
        }
        return list;
    }
```

## 5.Sax解析

- SAX是一个解析速度快并且占用内存少的xml解析器，SAX解析XML文件采用的是事件驱动，它并不需要解析完整个文档，而是按内容顺序解析文档的过程（不推荐）。

```java
public List<Student> sax2xml(InputStream is) throws Exception {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        //初始化Sax解析器
        SAXParser sp = spf.newSAXParser();
        //新建解析处理器
       MyHandler handler = new MyHandler();
        //将解析交给处理器
        sp.parse(is, handler);
        //返回List
        return handler.getList();
    }

    public class MyHandler extends DefaultHandler {

        private List<Student> list;
        private Student student;
        //用于存储读取的临时变量
        private String tempString;

        /**
         * 解析到文档开始调用，一般做初始化操作
         *
         * @throws SAXException
         */
        @Override
        public void startDocument() throws SAXException {
            list = new ArrayList<>();
            super.startDocument();
        }

        /**
         * 解析到文档末尾调用，一般做回收操作
         *
         * @throws SAXException
         */
        @Override
        public void endDocument() throws SAXException {
            super.endDocument();
        }

        /**
         * 每读到一个元素就调用该方法
         *
         * @param uri
         * @param localName
         * @param qName
         * @param attributes
         * @throws SAXException
         */
        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            if ("student".equals(qName)) {
                //读到student标签
                student = new Student();
            } else if ("name".equals(qName)) {
                //获取name里面的属性
                String sex = attributes.getValue("sex");
                student.setSex(sex);
            }
            super.startElement(uri, localName, qName, attributes);
        }

        /**
         * 读到元素的结尾调用
         *
         * @param uri
         * @param localName
         * @param qName
         * @throws SAXException
         */
        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if ("student".equals(qName)) {
                list.add(student);
            }
            if ("name".equals(qName)) {
                student.setName(tempString);
            } else if ("nickName".equals(qName)) {
                student.setNickName(tempString);
            }
            super.endElement(uri, localName, qName);
        }

        /**
         * 读到属性内容调用
         *
         * @param ch
         * @param start
         * @param length
         * @throws SAXException
         */
        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            tempString = new String(ch, start, length);
            super.characters(ch, start, length);
        }

        /**
         * 获取该List
         *
         * @return
         */
        public List<Student> getList() {
            return list;
        }
    }
```


## 6.三种方法的对比

- SAX解析器的工作方式是自动将事件推入事件处理器进行处理，因此你不能控制事件的处理主动结束；而Pull解析器的工作方式为允许你的应用程序代码**主动**从解析器中获取事件，因此可以在满足了需要的条件后不再获取事件，结束解析。
- SAX解析器的优点是解析速度快，占用内存少。非常适合在Android移动设备中使用。
- 由于DOM在内存中以树形结构存放，因此检索和更新效率会更高。但是对于特别大的文档，解析和加载整个文档将会很耗资源。
- PULL解析器的运行方式和SAX类似。PULL解析器小巧轻便，解析速度快，简单易用，非常适合在Android移动设备中使用，Android系统内部在解析各种XML时也是用PULL解析器。