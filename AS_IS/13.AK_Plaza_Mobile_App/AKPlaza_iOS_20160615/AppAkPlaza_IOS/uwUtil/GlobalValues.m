//
//  GlobalValues.m
//  AppAkMall_iOS
//
//  Created by uniwis on 11. 6. 27..
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//

#import "GlobalValues.h"
#import "CommonURL.h"


@implementation GlobalValues
@synthesize tabBar;
@synthesize majorIOSVersion;
@synthesize returnUrl;//p65458 20130711 return url add
@synthesize actUrl;//p65458 20130808 영수증 이전페이지 변경 문제 add

@synthesize pushUrl;//p65458 20130808 push 로 페이지 이동시 필요함 add

static GlobalValues * _globalValues = nil;

-(id)init
{
    return self;
}

+(GlobalValues *)sharedSingleton
{
    @synchronized([GlobalValues class])
    {
        if(!_globalValues)
            [[self alloc] init];
        
        
        return _globalValues;
    }
    
    return nil;
}


+(id)alloc
{
    @synchronized([GlobalValues class])
    {
        _globalValues = [super alloc];
        
        return _globalValues;
        
    }
    return nil;
}

+ (NSInteger)getNotice
{
    NSString *userDeviceToken = [[NSUserDefaults standardUserDefaults] objectForKey:@"deviceToken"];
    NSURL *url = [NSURL URLWithString:[[NSString stringWithFormat:@"%@?act=pushCount&token=%@",kLibURL, userDeviceToken] stringByReplacingOccurrencesOfString:@"http://" withString:kHTTP]];
    
    SBJsonParser *parser = [[SBJsonParser alloc]init];
    NSURLRequest *request = [NSURLRequest requestWithURL:url];
    
    NSData *response = [NSURLConnection sendSynchronousRequest:request returningResponse:nil error:nil];
    
    NSString *json_string = [[NSString alloc]initWithData:response encoding:NSUTF8StringEncoding];
    
    NSDictionary *statuses = [parser objectWithString:json_string error:nil];
    
    return [[statuses objectForKey:@"count"] intValue];
}

    

@end
