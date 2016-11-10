package com.anno;

import com.google.auto.service.AutoService;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

/**
 * author：buxiaoqing on 2016/11/9 19:02
 * Just do IT(没有梦想,何必远方)
 */
@AutoService(Processor.class)
public class MyProcessor extends AbstractProcessor {

    public static final String SUFFIX = "AutoGenerate";
    public static final String PREFIX = "My_";

    private Set<String> supportedAnnotationTypes = new HashSet<String>();
    private Elements elementUtils;


    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        System.out.println("MyProcessor init .........");
        supportedAnnotationTypes.add(PrintMe.class.getCanonicalName());
        elementUtils = processingEnv.getElementUtils();

    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        System.out.println("Test log int processing ...");
        Messager messager = processingEnv.getMessager();
        for (TypeElement te : annotations) {
            for (Element e : roundEnv.getElementsAnnotatedWith(te)) {
                // 准备在gradle的控制台打印信息
                // 打印
                messager.printMessage(Diagnostic.Kind.NOTE, "Printing: " + e.toString());
                messager.printMessage(Diagnostic.Kind.NOTE, "Printing: " + e.getSimpleName());
                messager.printMessage(Diagnostic.Kind.NOTE, "Printing: " + e.getEnclosingElement().toString());

                // 获取注解
                PrintMe annotation = e.getAnnotation(PrintMe.class);

                // 获取元素名并将其首字母大写
                String name = e.getSimpleName().toString();
                char c = Character.toUpperCase(name.charAt(0));
                name = String.valueOf(c + name.substring(1));

                // 包裹注解元素的元素, 也就是其父元素, 比如注解了成员变量或者成员函数, 其上层就是该类
                Element enclosingElement = e.getEnclosingElement();
                // 获取父元素的全类名, 用来生成包名
                String enclosingQualifiedName;
                if (enclosingElement instanceof PackageElement) {
                    enclosingQualifiedName = ((PackageElement) enclosingElement).getQualifiedName().toString();
                } else {
                    enclosingQualifiedName = ((TypeElement) enclosingElement).getQualifiedName().toString();
                }
                try {
                    // 生成的包名
                    String genaratePackageName = enclosingQualifiedName.substring(0, enclosingQualifiedName.lastIndexOf('.'));
                    // 生成的类名
                    String genarateClassName = PREFIX + enclosingElement.getSimpleName() + SUFFIX;

                    // 创建Java文件
                    JavaFileObject f = processingEnv.getFiler().createSourceFile(genarateClassName);
                    // 在控制台输出文件路径
                    messager.printMessage(Diagnostic.Kind.NOTE, "Printing: " + f.toUri());
                    Writer w = f.openWriter();
                    try {
                        PrintWriter pw = new PrintWriter(w);
                        pw.println("package " + genaratePackageName + ";");
                        pw.println("\npublic class " + genarateClassName + " { ");
                        pw.println("\n    /** 打印值 */");
                        pw.println("    public static void print" + name + "() {");
                        pw.println("        // 注解的父元素: " + enclosingElement.toString());
                        pw.println("        System.out.println(\"代码生成的路径: " + f.toUri() + "\");");
                        pw.println("        System.out.println(\"注解的元素: " + e.toString() + "\");");
                        pw.println("        System.out.println(\"注解的值: " + annotation.value() + "\");");
                        pw.println("    }");
                        pw.println("}");
                        pw.flush();
                    } finally {
                        w.close();
                    }
                } catch (IOException x) {
                    processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                            x.toString());
                }
            }
        }
        return true;

    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return supportedAnnotationTypes;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }
}
