#include <jni.h>
#include <iostream>
#include<string>
#include "NativeService.h"
#include<windows.h>
using namespace std;

void throw_java_exceptions(JNIEnv *env,string exception);

JNIEXPORT jstring JNICALL Java_NativeService_insert(JNIEnv *env, jobject thisobj, jobject user){
  if(NULL==user){
    throw_java_exceptions(env,"null");
    return NULL;
  }

  jclass userClass=env->GetObjectClass(user);
  if(NULL==userClass){
    throw_java_exceptions(env,"class");
    return NULL;
  }

  jmethodID classInit =  env->GetMethodID(userClass, "<init>", "()V");
  jmethodID getId =  env->GetMethodID(userClass, "getId", "()I");
  jmethodID getName =  env->GetMethodID(userClass, "getName", "()Ljava/lang/String;");
  jmethodID getPercentage =  env->GetMethodID(userClass, "getPercentage", "()D");
  if(NULL==classInit || NULL==getId || NULL==getName || NULL==getPercentage){
    throw_java_exceptions(env,"method");
    return NULL;
  }

    jobject obj=env->NewObject(userClass, classInit);
    obj=user;

    jint id=env->CallIntMethod(obj,getId);
    jstring tname=(jstring)env->CallObjectMethod(obj,getName);
    jdouble percentage=env->CallDoubleMethod(obj,getPercentage);
    const char *name = env->GetStringUTFChars(tname, NULL);
    printf("%d %s %f\n", id,name,percentage);
    FILE *fp=fopen("C:\\Program Files (x86)\\Apache Software Foundation\\Tomcat 8.0\\webapps\\jni-app-server\\files\\record.txt","a");
    if(fp){
      fprintf(fp,"Id:%d\tName:%s\tPercentage:%f\n",id,name,percentage);
      fclose(fp);
    }
    else{
      throw_java_exceptions(env,"file");
      return NULL;
    }
  jboolean flag = env->ExceptionCheck();
  if (flag) {
    env->ExceptionClear();
    throw_java_exceptions(env,"0");
    return NULL;
  }
  return env->NewStringUTF("success");
}

JNIEXPORT jstring JNICALL Java_NativeService_insertList(JNIEnv *env, jobject thisObj, jobject arr, jobject user){

  if(NULL==user){
    throw_java_exceptions(env,"null");
    return NULL;
  }

  jclass arrayClass = env->FindClass( "java/util/ArrayList");
  jclass thisClass=env->GetObjectClass(user);
  if(NULL==arrayClass || NULL==thisClass){
    throw_java_exceptions(env,"class");
    return NULL;
  }

  jmethodID arrayInit =  env->GetMethodID( arrayClass, "<init>", "()V");
  jmethodID arrayGet = env->GetMethodID( arrayClass, "get", "(I)Ljava/lang/Object;");
  jmethodID arraySize = env->GetMethodID( arrayClass, "size", "()I");

  jmethodID classInit =  env->GetMethodID( thisClass, "<init>", "()V");
  jmethodID getId =  env->GetMethodID( thisClass, "getId", "()I");
  jmethodID getName =  env->GetMethodID( thisClass, "getName", "()Ljava/lang/String;");
  jmethodID getPercentage =  env->GetMethodID( thisClass, "getPercentage", "()D");

  if(NULL==arrayInit|| NULL==arrayGet || NULL==arraySize || NULL==classInit || NULL==getId || NULL==getName || NULL==getName || NULL==getPercentage){
    throw_java_exceptions(env,"method");
    return NULL;
  }

  jobject objArr = env->NewObject( arrayClass, arrayInit);
  objArr=arr;
  jint size=env->CallIntMethod( objArr, arraySize);
  printf("%d\n",size );



  FILE *fp=fopen("C:\\Program Files (x86)\\Apache Software Foundation\\Tomcat 8.0\\webapps\\jni-app-server\\files\\record.txt","a");
  if(fp){
    for(jint i=0;i<size;i++){
      jobject obj=env->NewObject( thisClass, classInit);
      obj=env->CallObjectMethod( objArr, arrayGet,i);
      if(obj==NULL){
        throw_java_exceptions(env,"null");
        return NULL;
      }
      jint id=env->CallIntMethod(obj,getId);
      jstring tname=(jstring)env->CallObjectMethod(obj,getName);
      const char *name = env->GetStringUTFChars( tname, NULL);
      jdouble percentage=env->CallDoubleMethod(obj,getPercentage);
      fprintf(fp,"Id:%d\tName:%s\tPercentage:%f\n",id,name,percentage);
    }
    fclose(fp);
  }
  else{
    throw_java_exceptions(env,"file");
    return NULL;
  }
  jboolean flag = env->ExceptionCheck();
  if (flag) {
    env->ExceptionClear();
    throw_java_exceptions(env,"0");
  }
  return env->NewStringUTF("success");
}

JNIEXPORT jobject JNICALL Java_NativeService_view(JNIEnv *env, jobject thisObj,jobject user){

  printf("view\n");

  jclass thisClass=env->GetObjectClass(user);
  jclass arrayClass = env->FindClass( "java/util/ArrayList");
  if(thisClass==NULL || arrayClass==NULL){
    throw_java_exceptions(env,"class");
    return NULL;
  }

  jmethodID arrayInit =  env->GetMethodID( arrayClass, "<init>", "()V");
  jmethodID arrayAdd = env->GetMethodID( arrayClass, "add", "(Ljava/lang/Object;)Z");

  jmethodID classInit =  env->GetMethodID( thisClass, "<init>", "()V");
  jmethodID setId =  env->GetMethodID( thisClass, "setId", "(I)V");
  jmethodID setName =  env->GetMethodID( thisClass, "setName", "(Ljava/lang/String;)V");
  jmethodID setPercentage =  env->GetMethodID( thisClass, "setPercentage", "(D)V");
  if(NULL==arrayInit || NULL==arrayAdd || NULL==classInit || NULL==setId || NULL==setName || NULL==setPercentage){
    throw_java_exceptions(env,"method");
    return NULL;
  }
  jobject objArr = env->NewObject( arrayClass, arrayInit);
  char name[100];
  int id;
  jdouble percentage;

  FILE *fp=fopen("C:\\Program Files (x86)\\Apache Software Foundation\\Tomcat 8.0\\webapps\\jni-app-server\\files\\record.txt","r");
  if(fp){
    while (fscanf(fp,"Id:%d\tName:%s\tPercentage:%lf\n",&id, name, &percentage) != EOF){
      jobject obj=env->NewObject( thisClass, classInit);
      env->CallVoidMethod( obj, setId,(jint)id);
      env->CallVoidMethod( obj, setName,env->NewStringUTF(name));
      env->CallVoidMethod( obj, setPercentage,(jdouble)percentage);
      jboolean jbool=env->CallBooleanMethod( objArr, arrayAdd,obj);
    }
    fclose(fp);
  }
  else{
    throw_java_exceptions(env,"file");
    return NULL;
  }
  return objArr;
}

void throw_java_exceptions(JNIEnv *env,string exception){
  if(exception=="method"){
    jclass jcls =env->FindClass("javax/el/MethodNotFoundException");
    env->ThrowNew(jcls, "Exception in Native Method");
  }
  else if(exception=="class"){
    jclass jcls =env->FindClass("java/lang/ClassNotFoundException");
    env->ThrowNew(jcls, "Exception in Native Method");
  }
  else if(exception=="file"){
    jclass jcls =env->FindClass("java/io/FileNotFoundException");
    env->ThrowNew(jcls, "Exception in Native Method");
  }
  else if(exception=="null"){
    jclass jcls =env->FindClass("java/lang/NullPointerException");
    env->ThrowNew(jcls, "Exception in Native Method");
  }
  else{
    jclass jcls =env->FindClass("java/lang/Exception");
    env->ThrowNew(jcls, "Exception in Native Method");
  }
}
