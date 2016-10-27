//
//  AdBrix.h
//  AdBrixLib
//
//  Created by wonje,song on 2015. 5. 21..
//  Copyright (c) 2015년 wonje,song. All rights reserved.
//

#import <Foundation/Foundation.h>

#import <UIKit/UIKit.h>

typedef NS_ENUM(NSInteger, AdBrixCustomCohortType)
{
    AdBrixCustomCohort_1 = 1,
    AdBrixCustomCohort_2 = 2,
    AdBrixCustomCohort_3 = 3
};


@interface AdBrix : NSObject

/*!
 @abstract
 singleton AdBrix 객체를 반환한다.
 */
+ (AdBrix *)shared;

/*!
 @abstract
 first time experience의 Activity에 해당할때 호출한다.
 
 @param activityName              activity name.
 */
+ (void)firstTimeExperience:(NSString *)activityName;


/*!
 @abstract
 first time experience의 Activity에 해당할때 호출한다.
 
 @param activityName              activity name.
 @param param                     parameter.
 */
+ (void)firstTimeExperience:(NSString *)activityName param:(NSString *)param;

/*!
 @abstract
 retension의 Activity에 해당할때 호출한다.
 
 @param activityName              activity name.
 */
+ (void)retention:(NSString *)activityName;

/*!
 @abstract
 retension의 Activity에 해당할때 호출한다.
 
 @param activityName              activity name.
 @param param                     parameter.
 */
+ (void)retention:(NSString *)activityName param:(NSString *)param;


/*!
 @abstract
 buy의 Activity에 해당할때 호출한다.
 
 @param activityName              activity name.
 */
+ (void)buy:(NSString *)activityName;

/*!
 @abstract
 buy의 Activity에 해당할때 호출한다.
 
 @param activityName              activity name.
 @param param                     parameter.
 */
+ (void)buy:(NSString *)activityName param:(NSString *)param;


+ (void)showViralCPINotice:(UIViewController *)viewController;

/*!
 @abstract
 cohort 분석시 호출한다.
 
 @param customCohortType          cohort type : AdBrixCustomCohortType
 @param filterName                filter Name
 */
+ (void)setCustomCohort:(AdBrixCustomCohortType)customCohortType filterName:(NSString *)filterName;


@end
