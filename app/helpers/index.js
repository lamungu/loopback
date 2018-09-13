import moment from 'moment';

export const getTime = (timeMs) => {
  return Math.floor(moment.duration(parseInt(timeMs)).asMinutes()) + ':' + (moment.duration(parseInt(timeMs)).seconds() < 10 ? '0' : '') + moment.duration(parseInt(timeMs)).seconds();
}