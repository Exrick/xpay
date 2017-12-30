// CountDown Clock
// Version   : 1.0.1
// Developer : Ekrem KAYA
// Website   : http://e-piksel.com
// GitHub    : https://github.com/epiksel/countdown

(function ($) {
	$.fn.countdown = function (options, callback) {
		var settings = $.extend({
			date: null,
			offset: null,
			day: 'Day',
			days: 'Days',
			hour: 'Hour',
			hours: 'Hours',
			minute: 'Minute',
			minutes: 'Minutes',
			second: 'Second',
			seconds: 'Seconds'
		}, options);

		// Throw error if date is not set
		if (!settings.date) {
			$.error('Date is not defined.');
		}

		// Throw error if date is set incorectly
		if (!Date.parse(settings.date)) {
			$.error('Incorrect date format, it should look like this, 12/24/2012 12:00:00.');
		}

		// Save container
		var container = this;

		/**
		 * Change client's local date to match offset timezone
		 * @return {Object} Fixed Date object.
		 */
		var currentDate = function () {
			// get client's current date
			var date = new Date();

			// turn date to utc
			var utc = date.getTime() + (date.getTimezoneOffset() * 60000);

			// set new Date object
			var new_date = new Date(utc + (3600000*settings.offset));

			return new_date;
		};

		/**
		 * Main countdown function that calculates everything
		 */
		function countdown () {
			var target_date = new Date(settings.date), // set target date
				current_date = currentDate(); // get fixed current date

			// difference of dates
			var difference = target_date - current_date;

			// if difference is negative than it's pass the target date
			if (difference < 0) {
				// stop timer
				clearInterval(interval);

				if (callback && typeof callback === 'function') callback();

				return;
			}

			// basic math variables
			var _second = 1000,
				_minute = _second * 60,
				_hour = _minute * 60,
				_day = _hour * 24;

			// calculate dates
			var days = Math.floor(difference / _day),
				hours = Math.floor((difference % _day) / _hour),
				minutes = Math.floor((difference % _hour) / _minute),
				seconds = Math.floor((difference % _minute) / _second);
		
			// based on the date change the refrence wording
			var text_days = (days === 1) ? settings.day : settings.days,
				text_hours = (hours === 1) ? settings.hour : settings.hours,
				text_minutes = (minutes === 1) ? settings.minute : settings.minutes,
				text_seconds = (seconds === 1) ? settings.second : settings.seconds;

				// fix dates so that it will show two digets
				days = (String(days).length >= 2) ? days : '0' + days;
				hours = (String(hours).length >= 2) ? hours : '0' + hours;
				minutes = (String(minutes).length >= 2) ? minutes : '0' + minutes;
				seconds = (String(seconds).length >= 2) ? seconds : '0' + seconds;

			// set to DOM
			container.find('.days').text(days);
			container.find('.hours').text(hours);
			container.find('.minutes').text(minutes);
			container.find('.seconds').text(seconds);

			container.find('.days_text').text(text_days);
			container.find('.hours_text').text(text_hours);
			container.find('.minutes_text').text(text_minutes);
			container.find('.seconds_text').text(text_seconds);
		}
		
		// start
		var interval = setInterval(countdown, 1000);
	};

})(jQuery);
