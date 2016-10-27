//
//  PhotoEventProc.h
//  AppAkPlaza_IOS
//
//  Created by 김 두민 on 12. 5. 22..
//  Copyright (c) 2012년 puuu80@uniwis.com. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ASIFormDataRequest.h"

@interface PhotoEventProc : NSObject


-(NSString*) submitPhotoEvent:(UIImage*)image KeyArr:(NSArray*)keyArr VarArr:(NSArray*)varArr;
-(NSDictionary*) getEntryDetailByAct:(NSString*)act event_index:(NSString*)event_index entry_indexno:(NSString*)entry_indexno;

@end
