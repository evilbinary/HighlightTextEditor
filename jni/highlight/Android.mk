LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE    := highlight

BOOST_VERSION :=1_53_0
BOOST_VERSION2 :=1_53

MY_LIB_PATH :=$(LOCAL_PATH)/../../../lib
BOOST_INCLUDE_PATH :=$(MY_LIB_PATH)/boost_$(BOOST_VERSION)/include
BOOST_LIB_PATH :=$(MY_LIB_PATH)/boost_$(BOOST_VERSION)/armeabi/lib

LUA_INCLUDE_PATH :=$(LOCAL_PATH)/../lua

LOCAL_C_INCLUDES := $(LOCAL_PATH)/include \
	$(BOOST_INCLUDE_PATH) \
	$(LUA_INCLUDE_PATH) \
	$(LOCAL_PATH)/android/ \
	$(LOCAL_PATH)/cli/ \
	
LOCAL_SRC_FILES := $(subst $(LOCAL_PATH)/,, \
	$(wildcard $(LOCAL_PATH)/core/*.cpp) \
	$(wildcard $(LOCAL_PATH)/core/astyle/*.cpp) \
	$(wildcard $(LOCAL_PATH)/core/Diluculum/*.cpp)  \
	$(wildcard $(LOCAL_PATH)/android/*.cpp) \
	$(wildcard $(LOCAL_PATH)/cli/*.cpp) \
	$(wildcard $(LOCAL_PATH)/cli/*.cc) \
	$(wildcard $(LOCAL_PATH)/android/*.cc)  ) \

LOCAL_LDLIBS += -L$(BOOST_LIB_PATH)     \
                -lboost_system-gcc-mt-$(BOOST_VERSION2)  \
                -lboost_thread-gcc-mt-$(BOOST_VERSION2) \
                
LOCAL_SHARED_LIBRARIES := lua
#LOCAL_STATIC_LIBRARIES := lua

LOCAL_LDLIBS := -llog
LOCAL_CFLAGS += 
LOCAL_CPPFLAGS := -std=c++11  -fpermissive -fexceptions -g -DANDROID


LOCAL_ARM_MODE := arm

include $(BUILD_SHARED_LIBRARY)
#include $(BUILD_EXECUTABLE)
