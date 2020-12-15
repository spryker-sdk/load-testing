/*
 * Parses URI search string into Object
 * Inspired by http://unixpapa.com/js/querystring.html
 *
 * @param {String} uri URI search string to parse
 * @param {Function} x parser for property value
 * @param {Function} y parser for property name
 * @returns {Object}
 */
function parseURISearchParams(uri, x, y) {
    x = x || function(x) {
        x = decodeURIComponent(x && x.replace(/\+/g, ' '));
        return (!isNaN(parseInt(x)) && !isNaN(x * 1)) ? x * 1 : x;
    };
    y = y || decodeURIComponent;

    var o = {};

    uri = (uri + '').split('?')[1] || '';

    if (uri === '') {
        return o;
    }

    function add(o, k, v) {
        if (o.hasOwnProperty(k)) {
            if (o[k] instanceof Array) {
                o[k].push(v);
            } else if (!(o[k] instanceof Object)) {
                o[k] = [o[k], v];
            }
        } else {
            o[k] = v;
        }
        o = o[k];
        return o;
    }

    return uri.match(/([^=&]+)(=([^&]*))?/g).reduce(function(c, p, i, d) {
        i = /([^=]+)(=(.*))?/.exec(p)
        var k = y(i[1]),
            v = i[3] || '';

        if (/\[/.test(k)) {
            k.match(/([^\[]+)|\[([^\]]+)\]/g).reduce(function(c, p, i, d) {
                return add(c, p.replace(/[\[\]]/g, ''), (d.length - 1 === i ? x(v) : {}));
            }, o);
        } else {
            add(c, k, x(v));
        }
        return o;
    }, o);
}
