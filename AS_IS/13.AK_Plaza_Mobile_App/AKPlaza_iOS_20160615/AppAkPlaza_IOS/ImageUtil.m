//
//  ImageUtil.m
//  AppAkPlaza_IOS
//
//  Created by 김 두민 on 12. 5. 24..
//  Copyright (c) 2012년 puuu80@uniwis.com. All rights reserved.
//

#import "ImageUtil.h"

@implementation ImageUtil

+ (UIImage*)imageWithImage:(UIImage*)image scaledToSize:(CGSize)newSize
{
    UIGraphicsBeginImageContext( newSize );
    [image drawInRect:CGRectMake(0,0,newSize.width,newSize.height)];
    UIImage* newImage = UIGraphicsGetImageFromCurrentImageContext();
    UIGraphicsEndImageContext();
    
    return newImage;
}

@end
