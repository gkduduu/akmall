//
//  MD5Util.m
//  AppAkMall_iOS
//
//  Created by uniwis on 11. 8. 24..
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//
#import <CommonCrypto/CommonDigest.h>

#import "MD5Util.h"

@implementation MD5Util

+ (NSString*)MD5:(NSString *)inStr
{
    const char *inUTF8_str = [inStr UTF8String];
    
    unsigned char result[CC_MD5_DIGEST_LENGTH];
    
    CC_MD5(inUTF8_str, strlen(inUTF8_str), result);
    
    NSMutableString *hash = [NSMutableString string];
    
    for (int i=0; i < CC_MD5_DIGEST_LENGTH; i++) {
        [hash appendFormat:@"%02X", result[i]];
    }    
    
    return [hash lowercaseString];
}


@end
