//
// Created by Xfast on 2016/7/21.
//

#ifndef NDK_LOG_H
#define NDK_LOG_H

#include <android/log.h>

#define TAG "VA-IO"

#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,  TAG, __VA_ARGS__)
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,  TAG, __VA_ARGS__)
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR, TAG, __VA_ARGS__)

#define FREE(ptr, org_ptr) { if ((void*) ptr != NULL && (void*) ptr != (void*) org_ptr) { free((void*) ptr); } }

#define NELEM(x) ((int) (sizeof(x) / sizeof((x)[0])))

#define NATIVE_METHOD(func_ptr, func_name, signature) { func_name, signature, reinterpret_cast<void*>(func_ptr) }

#define JAVA_CLASS "com/lody/virtual/IOHook"

#define JAVA_CALLBACK__ON_KILL_PROCESS "onKillProcess"
#define JAVA_CALLBACK__ON_KILL_PROCESS_SIGNATURE "(II)V"

#define ANDROID_L    21


#endif //NDK_LOG_H
