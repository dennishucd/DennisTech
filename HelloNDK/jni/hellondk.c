/*
 * hello_ndk.c
 *
 *  Created on: 2014-3-25
 *      Author: dennishu
 */
#include <jni.h>

JNIEXPORT jint JNICALL Java_cn_dennishucd_HelloNDKActivity_add
  (JNIEnv * env, jobject obj, jint a, jint b)

{
    return a+b;
}
