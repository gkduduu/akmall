//
//  PhotoEventProc.m
//  AppAkPlaza_IOS
//
//  Created by 김 두민 on 12. 5. 22..
//  Copyright (c) 2012년 puuu80@uniwis.com. All rights reserved.
//

#import "PhotoEventProc.h"
#import "CommonURL.h"
#import "JSON.h"

@implementation PhotoEventProc


-(NSString*) submitPhotoEvent:(UIImage*)image
                       KeyArr:(NSArray*)keyArr 
                       VarArr:(NSArray*)varArr
{   
    NSURL *url = [NSURL URLWithString:kLibURL];
    ASIFormDataRequest *request = [ASIFormDataRequest requestWithURL:url];
    
    // set Content-Type in HTTP header
    NSString *boundary = [NSString stringWithFormat:@"0xKhTmLbOuNdArY"];
    NSString *contentType = [NSString stringWithFormat:@"multipart/form-data; boundary=%@", boundary];
    
    [request setRequestMethod:@"POST"];    
    [request addRequestHeader:@"Content-type" value:contentType];
    [request addRequestHeader:@"makp-device" value:@"iOS"];
    [request addRequestHeader:@"makp-version" value:[[[NSBundle mainBundle] infoDictionary] objectForKey:@"CFBundleVersion"]];
    
    //create the post body
    NSMutableData *body = [NSMutableData data];
    
    NSData *imageData = UIImageJPEGRepresentation(image, 0.6f);
    
    for (int i=0; i<[keyArr count]; i++) {
        [body appendData:[[NSString stringWithFormat:@"--%@\r\n", boundary] dataUsingEncoding:NSUTF8StringEncoding]];
        [body appendData:[[NSString stringWithFormat:@"Content-Disposition: form-data; name=\"%@\"\r\n\r\n", [keyArr objectAtIndex:i]] dataUsingEncoding:NSUTF8StringEncoding]];
        [body appendData:[[NSString stringWithFormat:@"%@\r\n", [varArr objectAtIndex:i]] dataUsingEncoding:NSUTF8StringEncoding]];
    }
    
    if (imageData) {
        [body appendData:[[NSString stringWithFormat:@"--%@\r\n", boundary] dataUsingEncoding:NSUTF8StringEncoding]];
        [body appendData:[[NSString stringWithFormat:@"Content-Disposition: form-data; name=\"attach\"; filename=\"image.jpg\"\r\n"] dataUsingEncoding:NSUTF8StringEncoding]];
        [body appendData:[[NSString stringWithFormat:@"Content-Type: image/jpeg\r\n\r\n"] dataUsingEncoding:NSUTF8StringEncoding]];
        [body appendData:imageData];
        [body appendData:[[NSString stringWithFormat:@"\r\n"] dataUsingEncoding:NSUTF8StringEncoding]];
    }
    [body appendData:[[NSString stringWithFormat:@"--%@--\r\n", boundary] dataUsingEncoding:NSUTF8StringEncoding]];
    
    [request setTimeOutSeconds:60];
    [request setPostBody:body];
    [request startSynchronous];

    NSString *reString = @"";
    if ([request error]){
        reString = @"fail&splt;네트워크 오류가 발생하였습니다.";
    }else{
        NSString *response = [request responseString];
        NSMutableDictionary *responseJSON = [response JSONValue];   
        reString = [NSString stringWithFormat:@"%@&splt;%@&splt;%@",[responseJSON objectForKey:@"result"],[responseJSON objectForKey:@"message"],[responseJSON objectForKey:@"refresh"]];

    }
    
    return reString;
}


-(NSMutableDictionary*) getEntryDetailByAct:(NSString*)act event_index:(NSString*)event_index entry_indexno:(NSString*)entry_indexno
{
    NSURL *url = [NSURL URLWithString:kEventURL];
    
    ASIFormDataRequest *request = [ASIFormDataRequest requestWithURL:url];
    [request setRequestMethod:@"POST"];
    
    [request setPostValue:act forKey:@"act"];
    [request setPostValue:event_index forKey:@"event_index"];
    [request setPostValue:entry_indexno forKey:@"entry_indexno"];
    [request startSynchronous];
    
    if ([request error])
    {
        
    }
    NSString *responseString = [request responseString];
    return [responseString JSONValue];
}



@end
